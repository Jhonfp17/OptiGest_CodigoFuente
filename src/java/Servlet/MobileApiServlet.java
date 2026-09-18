package Servlet;

import Controlador.ActivosDAO;
import Controlador.AsignacionesDAO;
import Controlador.PersonalDAO;
import Modelo.Activos;
import Modelo.AsignacionConsulta;
import Modelo.Personal;
import Servicio.AutenticacionService;
import Servicio.AutenticacionService.ResultadoAutenticacion;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * API REST para la aplicación móvil Flutter.
 *
 * Flutter se comunica únicamente con este servlet mediante POST y JSON.
 *
 * Arquitectura:
 *
 * Flutter
 *    ↓
 * MobileApiServlet
 *    ↓
 * Servicios / DAO
 *    ↓
 * Base de datos
 */
@WebServlet(
        name = "MobileApiServlet",
        urlPatterns = {"/api/Mobile"}
)
public class MobileApiServlet extends HttpServlet {

    private final AutenticacionService autenticacionService =
            new AutenticacionService();

    private final ActivosDAO activosDAO =
            new ActivosDAO();

    private final PersonalDAO personalDAO =
            new PersonalDAO();

    private final AsignacionesDAO asignacionesDAO =
            new AsignacionesDAO();

    /**
     * Único método utilizado por la API.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCors(response);
        response.setContentType("application/json;charset=UTF-8");

        try (JsonReader reader = Json.createReader(request.getReader())) {

            JsonObject datos = reader.readObject();

            String accion = obtenerTexto(datos, "accion");

            if (accion == null || accion.trim().isEmpty()) {

                responderError(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Debes indicar la accion."
                );

                return;
            }

            switch (accion.toLowerCase()) {

                case "login":
                    procesarLogin(datos, response);
                    break;

                case "activos":
                    procesarActivos(datos, response);
                    break;

                case "personal":
                    procesarPersonal(datos, response);
                    break;

                case "asignaciones":
                    procesarAsignaciones(datos, response);
                    break;

                default:

                    responderError(
                            response,
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Accion no valida."
                    );
            }

        } catch (JsonException | ClassCastException e) {

            responderError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "El cuerpo debe ser un objeto JSON valido."
            );

        } catch (Exception e) {

            e.printStackTrace();

            responderError(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Ocurrio un error en el servidor."
            );
        }
    }

    /**
     * La API solamente permite POST.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCors(response);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setHeader("Allow", "POST");

        responderError(
                response,
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Esta API utiliza solamente POST."
        );
    }

    /**
     * Responde la verificacion previa del navegador para Flutter Web.
     * La API no usa cookies ni sesiones de navegador.
     */
    @Override
    protected void doOptions(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCors(response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void procesarLogin(
            JsonObject datos,
            HttpServletResponse response)
            throws IOException {

        String usuario = obtenerTexto(datos, "usuario");
        String password = obtenerTexto(datos, "password");

        ResultadoAutenticacion resultado =
                autenticacionService.autenticar(usuario, password);

        if (resultado.esExitoso()) {

            Personal personal = resultado.getPersonal();

            /*
             * No enviamos los IDs internos de la base de datos.
             */
            JsonObject usuarioJson =
                    Json.createObjectBuilder()
                            .add(
                                    "identificacion",
                                    textoSeguro(
                                            personal.getIdentificacion()
                                    )
                            )
                            .add(
                                    "nombre",
                                    textoSeguro(
                                            personal.getNombre()
                                    )
                            )
                            .add(
                                    "apellidos",
                                    textoSeguro(
                                            personal.getApellidos()
                                    )
                            )
                            .add(
                                    "email",
                                    textoSeguro(
                                            personal.getEmail()
                                    )
                            )
                            .add(
                                    "tipoAcceso",
                                    textoSeguro(
                                            personal.getTipo_acceso()
                                    )
                            )
                            .add(
                                    "esAdministrador",
                                    personal.esAdministrador()
                            )
                            .build();

            JsonObject respuesta =
                    Json.createObjectBuilder()
                            .add("ok", true)
                            .add("usuario", usuarioJson)
                            .build();

            responderJson(
                    response,
                    HttpServletResponse.SC_OK,
                    respuesta
            );

            return;
        }

        if (resultado.getEstado()
                == ResultadoAutenticacion.Estado.CAMPOS_INCOMPLETOS) {

            responderError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Completa usuario y password."
            );

        } else if (resultado.getEstado()
                == ResultadoAutenticacion.Estado.SIN_PERMISO) {

            responderError(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "No tienes permiso para acceder."
            );

        } else {

            responderError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Credenciales invalidas."
            );
        }
    }

    // =========================================================
    // ACTIVOS
    // =========================================================

    private void procesarActivos(
            JsonObject datos,
            HttpServletResponse response)
            throws IOException {

        String campo = obtenerTexto(datos, "campo");
        String valor = obtenerTexto(datos, "valor");

        /*
         * El DAO se encarga de obtener los activos
         * desde la base de datos.
         */
        List<Activos> activos = activosDAO.listar();

        JsonArrayBuilder lista =
                Json.createArrayBuilder();

        for (Activos activo : activos) {

            /*
             * Si no hay búsqueda, se devuelven todos.
             * Si existe búsqueda, se filtra en el backend.
             */
            if (!coincideActivo(activo, campo, valor)) {
                continue;
            }

            JsonObjectBuilder item =
                    Json.createObjectBuilder()
                            .add(
                                    "codigo",
                                    textoSeguro(
                                            activo.getCodigo_act()
                                    )
                            )
                            .add(
                                    "nombre",
                                    textoSeguro(
                                            activo.getNombre_activos()
                                    )
                            )
                            .add(
                                    "valor",
                                    textoSeguro(
                                            activo.getValor()
                                    )
                            )
                            .add(
                                    "fechaAdquisicion",
                                    textoSeguro(
                                            activo.getFecha_adquma()
                                    )
                            )
                            .add(
                                    "fechaDevolucion",
                                    textoSeguro(
                                            activo.getFecha_devolucion()
                                    )
                            )
                            .add(
                                    "vidaUtil",
                                    activo.getVida_util()
                            )
                            .add(
                                    "descripcion",
                                    textoSeguro(
                                            activo.getDescripcion()
                                    )
                            );

            lista.add(item);
        }

        JsonObject respuesta =
                Json.createObjectBuilder()
                        .add("ok", true)
                        .add("data", lista)
                        .build();

        responderJson(
                response,
                HttpServletResponse.SC_OK,
                respuesta
        );
    }

    private boolean coincideActivo(
            Activos activo,
            String campo,
            String valor) {

        /*
         * Sin búsqueda:
         * se muestran todos los activos.
         */
        if (campo == null
                || valor == null
                || valor.trim().isEmpty()) {

            return true;
        }

        String texto = valor.toLowerCase().trim();

        switch (campo.toLowerCase()) {

            case "codigo":

                return textoSeguro(
                        activo.getCodigo_act()
                )
                        .toLowerCase()
                        .contains(texto);

            case "nombre":

                return textoSeguro(
                        activo.getNombre_activos()
                )
                        .toLowerCase()
                        .contains(texto);

            case "estado":

                /*
                 * Actualmente el modelo Activos contiene
                 * el ID del estado.
                 *
                 * No se envía este ID a Flutter.
                 */
                return String.valueOf(
                        activo.getEstado_Activo_idEstado_Activo()
                ).contains(texto);

            default:

                return true;
        }
    }

    // =========================================================
    // PERSONAL
    // =========================================================

    private void procesarPersonal(
            JsonObject datos,
            HttpServletResponse response)
            throws IOException {

        String campo = obtenerTexto(datos, "campo");
        String valor = obtenerTexto(datos, "valor");

        /*
         * PersonalDAO consulta la información
         * directamente desde la base de datos.
         */
        List<Personal> personal = personalDAO.listar();

        JsonArrayBuilder lista =
                Json.createArrayBuilder();

        for (Personal persona : personal) {

            if (!coincidePersonal(persona, campo, valor)) {
                continue;
            }

            JsonObjectBuilder item =
                    Json.createObjectBuilder()
                            .add(
                                    "nombre",
                                    textoSeguro(
                                            persona.getNombre()
                                    )
                            )
                            .add(
                                    "apellidos",
                                    textoSeguro(
                                            persona.getApellidos()
                                    )
                            )
                            .add(
                                    "documento",
                                    textoSeguro(
                                            persona.getIdentificacion()
                                    )
                            )
                            .add(
                                    "correo",
                                    textoSeguro(
                                            persona.getEmail()
                                    )
                            );

            lista.add(item);
        }

        JsonObject respuesta =
                Json.createObjectBuilder()
                        .add("ok", true)
                        .add("data", lista)
                        .build();

        responderJson(
                response,
                HttpServletResponse.SC_OK,
                respuesta
        );
    }

    private boolean coincidePersonal(
            Personal persona,
            String campo,
            String valor) {

        if (campo == null
                || valor == null
                || valor.trim().isEmpty()) {

            return true;
        }

        String texto = valor.toLowerCase().trim();

        switch (campo.toLowerCase()) {

            case "documento":

                return textoSeguro(
                        persona.getIdentificacion()
                )
                        .toLowerCase()
                        .contains(texto);

            case "nombre":

                String nombreCompleto =
                        textoSeguro(persona.getNombre())
                                + " "
                                + textoSeguro(persona.getApellidos());

                return nombreCompleto
                        .toLowerCase()
                        .contains(texto);

            case "correo":

                return textoSeguro(
                        persona.getEmail()
                )
                        .toLowerCase()
                        .contains(texto);

            default:

                return true;
        }
    }

    // =========================================================
    // ASIGNACIONES
    // =========================================================

    private void procesarAsignaciones(
            JsonObject datos,
            HttpServletResponse response)
            throws IOException {

        String campo = obtenerTexto(datos, "campo");
        String valor = obtenerTexto(datos, "valor");

        List<AsignacionConsulta> asignaciones;

        /*
         * Sin filtro:
         * obtenemos las asignaciones desde el DAO.
         */
        if (campo == null
                || valor == null
                || valor.trim().isEmpty()) {

            asignaciones =
                    asignacionesDAO.listarUltimasConsulta(0);

        } else {

            /*
             * El DAO se encarga de hacer la consulta.
             */
            asignaciones =
                    asignacionesDAO.consultarPorFiltroYRango(
                            campo,
                            valor,
                            null,
                            null
                    );
        }

        JsonArrayBuilder lista =
                Json.createArrayBuilder();

        for (AsignacionConsulta asignacion
                : asignaciones) {

            /*
             * No enviamos idAsignacion.
             */
            JsonObjectBuilder item =
                    Json.createObjectBuilder()
                            .add(
                                    "documento",
                                    textoSeguro(
                                            asignacion.getIdentificacion()
                                    )
                            )
                            .add(
                                    "nombrePersonal",
                                    textoSeguro(
                                            asignacion.getNombrePersona()
                                    )
                            )
                            .add(
                                    "apellidosPersonal",
                                    textoSeguro(
                                            asignacion.getApellidosPersona()
                                    )
                            )
                            .add(
                                    "activo",
                                    textoSeguro(
                                            asignacion.getNombreActivo()
                                    )
                            )
                            .add(
                                    "codigoActivo",
                                    textoSeguro(
                                            asignacion.getCodigoActivo()
                                    )
                            )
                            .add(
                                    "fechaAsignacion",
                                    textoSeguro(
                                            asignacion.getFechaAsignacion()
                                    )
                            )
                            .add(
                                    "fechaDevolucion",
                                    textoSeguro(
                                            asignacion.getFechaDevolucion()
                                    )
                            )
                            .add(
                                    "observaciones",
                                    textoSeguro(
                                            asignacion.getObservaciones()
                                    )
                            );

            lista.add(item);
        }

        JsonObject respuesta =
                Json.createObjectBuilder()
                        .add("ok", true)
                        .add("data", lista)
                        .build();

        responderJson(
                response,
                HttpServletResponse.SC_OK,
                respuesta
        );
    }

    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    private String obtenerTexto(
            JsonObject datos,
            String nombre) {

        if (datos.containsKey(nombre)
                && !datos.isNull(nombre)) {

            return datos.getString(nombre, null);
        }

        return null;
    }

    private String textoSeguro(String valor) {
        return valor == null ? "" : valor;
    }

    /**
     * Permite que Flutter Web consuma esta API desde un origen distinto.
     */
    private void configurarCors(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader(
                "Access-Control-Allow-Headers",
                "Content-Type, Accept, Authorization"
        );
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    private void responderJson(
            HttpServletResponse response,
            int estado,
            JsonObject json)
            throws IOException {

        response.setStatus(estado);
        response.getWriter().write(json.toString());
    }

    private void responderError(
            HttpServletResponse response,
            int estado,
            String mensaje)
            throws IOException {

        JsonObject json =
                Json.createObjectBuilder()
                        .add("ok", false)
                        .add("mensaje", mensaje)
                        .build();

        responderJson(
                response,
                estado,
                json
        );
    }
}
