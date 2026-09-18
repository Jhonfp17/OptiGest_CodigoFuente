package Servlet;

import Controlador.DocumentoDAO;
import Controlador.Estado_PersonalDAO;
import Controlador.PersonalDAO;
import Controlador.RolesDAO;
import Controlador.ContactoDAO;
import Modelo.MensajeContacto;
import Modelo.Personal;
import Seguridad.AuditoriaContexto;
import Servicio.ValidacionPersonal;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "GuardarPersonal", urlPatterns = {"/GuardarPersonal"})
public class GuardarPersonal extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        Personal admin = (Personal) session.getAttribute("usuarioLogueado");

        if (!admin.esAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/VerPerfil");
            return;
        }

        cargarCombos(request);
        cargarSolicitudDeContacto(request);

        request.getRequestDispatcher("/Vista/RegistroPersonal.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        boolean origenConfiguracion = "configuracion".equals(request.getParameter("origen"));

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        Personal admin = (Personal) session.getAttribute("usuarioLogueado");

        if (!admin.esAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/VerPerfil");
            return;
        }

        try {
            Personal per = new Personal();

            per.setNombre(request.getParameter("nombre"));
            per.setApellidos(request.getParameter("apellidos"));
            per.setIdentificacion(request.getParameter("identificacion"));
            per.setEmail(request.getParameter("email"));
            per.setTelefono(request.getParameter("telefono"));
            per.setDireccion(request.getParameter("direccion"));
            // El formulario actual envía modo_clave=generada. Se conserva la
            // lectura del valor anterior para no romper formularios ya abiertos.
            boolean claveGenerada = "generada".equals(request.getParameter("modo_clave"))
                    || "1".equals(request.getParameter("clave_provisional"));
            String clave = request.getParameter("clave") == null ? "" : request.getParameter("clave").trim();
            if (claveGenerada) {
                clave = generarClaveInterna();
            }
            if (clave.isEmpty()) {
                request.setAttribute("mensaje", "Falta la contraseña inicial. Escribe una contraseña de 8 a 20 caracteres o selecciona “Generar contraseña segura”.");
                cargarCombos(request);
                request.getRequestDispatcher("/Vista/RegistroPersonal.jsp").forward(request, response);
                return;
            }
            per.setClave(clave);
            per.setObservaciones(request.getParameter("observaciones"));
            per.setFecha_contratacion(request.getParameter("fecha_contratacion"));

            // Una cuenta que se activa desde "Olvidé mi contraseña" debe poder ingresar
            // después de que la persona establezca su propia clave.
            per.setPuede_acceder("1".equals(request.getParameter("puede_acceder")));
            per.setDebeCambiarClave(claveGenerada);
            per.setClaveTemporalExpiraEn(claveGenerada ? LocalDateTime.now().plusMinutes(30) : null);
            per.setDocumento_id_documento(Integer.parseInt(request.getParameter("Documento_id_documento")));
            per.setRoles_idroles(Integer.parseInt(request.getParameter("roles_idroles")));
            per.setEstado_Personal_id_estado(Integer.parseInt(request.getParameter("Estado_Personal_id_estado")));

            DocumentoDAO documentoDAO = new DocumentoDAO();
            Modelo.Documento documento = documentoDAO.consultar(per.getDocumento_id_documento());
            String tipoDocumento = documento == null ? null : documento.getDescripcion_doc();
            ValidacionPersonal.normalizar(per);
            String errorValidacion = ValidacionPersonal.validar(per, tipoDocumento, !claveGenerada);
            if (errorValidacion != null) {
                request.setAttribute("mensaje", errorValidacion);
                cargarCombos(request);
                request.getRequestDispatcher("/Vista/RegistroPersonal.jsp").forward(request, response);
                return;
            }

            Long solicitudId = obtenerSolicitudId(request);
            ContactoDAO contactoDAO = new ContactoDAO();
            if (solicitudId != null && !contactoDAO.estaVigenteParaRegistro(solicitudId)) {
                request.setAttribute("mensaje", "La solicitud de contacto ya no está vigente para registrar personal.");
                cargarCombos(request);
                request.getRequestDispatcher("/Vista/RegistroPersonal.jsp").forward(request, response);
                return;
            }

            PersonalDAO dao = new PersonalDAO();

            AuditoriaContexto.establecer(admin, "Registro de personal");
            boolean guardado;
            try {
                guardado = dao.insertar(per);
            } finally {
                AuditoriaContexto.limpiar();
            }
            if (guardado) {
                if (solicitudId != null) {
                    Personal creado = dao.consultarPorIdentificacion(per.getIdentificacion());
                    if (creado != null) {
                        contactoDAO.asociarPersonal(solicitudId, creado.getIdPersonal());
                        if (claveGenerada) session.setAttribute("claveGeneradaPersonalId", creado.getIdPersonal());
                    }
                    contactoDAO.marcarAtendido(solicitudId);
                }
                if (claveGenerada) {
                    session.setAttribute("claveGenerada", clave);
                    session.setAttribute("claveGeneradaExpiraEn", per.getClaveTemporalExpiraEn().toString());
                }
                String destino = solicitudId != null
                        ? "/GestionContacto?personalRegistrado=1"
                        : (origenConfiguracion
                                ? "/GestionCatalogo?tipo=personal&guardado=1"
                                : "/GestionarPersonal?guardado=1");
                response.sendRedirect(request.getContextPath() + destino);
            } else {
                request.setAttribute("mensaje", "Error al registrar. Verifica los datos e intenta de nuevo.");
                cargarCombos(request);
                request.getRequestDispatcher("/Vista/RegistroPersonal.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            System.out.println("Error de formato numerico en GuardarPersonal: " + e.getMessage());

            request.setAttribute("mensaje", "Hay campos numericos invalidos. Revisa los selects del formulario.");
            cargarCombos(request);
            request.getRequestDispatcher("/Vista/RegistroPersonal.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.out.println("Error en GuardarPersonal doPost: " + e.getMessage());

            request.setAttribute("mensaje", "Ocurrio un error al registrar el personal.");
            cargarCombos(request);
            request.getRequestDispatcher("/Vista/RegistroPersonal.jsp")
                    .forward(request, response);
        }
    }

    private void cargarCombos(HttpServletRequest request) {
        request.setAttribute("documentos", new DocumentoDAO().listar());
        request.setAttribute("roles", new RolesDAO().listar());
        request.setAttribute("estadosPersonal", new Estado_PersonalDAO().listar());
    }

    private void cargarSolicitudDeContacto(HttpServletRequest request) {
        Long solicitudId = obtenerSolicitudId(request);
        if (solicitudId == null) return;

        MensajeContacto solicitud = new ContactoDAO().consultarPorId(solicitudId);
        if (solicitud == null || solicitud.isAtendido()
                || !new ContactoDAO().estaVigenteParaRegistro(solicitudId)
                || !"CREAR CUENTA".equals(normalizar(solicitud.getTipoSolicitud()))) {
            request.setAttribute("mensaje", "La solicitud seleccionada no está disponible para crear una cuenta.");
            return;
        }

        request.setAttribute("solicitudId", solicitudId);
        request.setAttribute("nombre", solicitud.getNombre());
        request.setAttribute("apellidos", solicitud.getApellidos());
        request.setAttribute("identificacion", solicitud.getDocumento());
        request.setAttribute("email", solicitud.getEmail());
        request.setAttribute("telefono", solicitud.getTelefono());
        request.setAttribute("direccion", solicitud.getDireccion());
        request.setAttribute("documentoSolicitud", solicitud.getTipoDocumento());
        request.setAttribute("usuarioSolicitado", solicitud.getUsuarioDeseado());
    }

    private Long obtenerSolicitudId(HttpServletRequest request) {
        try {
            String valor = request.getParameter("solicitudId");
            return valor == null || valor.trim().isEmpty() ? null : Long.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toUpperCase(Locale.forLanguageTag("es-CO"));
    }

    private String generarClaveInterna() {
        final String caracteres = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
        SecureRandom aleatorio = new SecureRandom();
        StringBuilder clave = new StringBuilder("Act-");
        for (int i = 0; i < 16; i++) {
            clave.append(caracteres.charAt(aleatorio.nextInt(caracteres.length())));
        }
        return clave.toString();
    }
}
