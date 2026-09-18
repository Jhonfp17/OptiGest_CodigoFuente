package Servlet;

import Controlador.ActivosDAO;
import Controlador.AsignacionesDAO;
import Controlador.CategoriasDAO;
import Controlador.DiasDAO;
import Controlador.DocumentoDAO;
import Controlador.Estado_ActivoDAO;
import Controlador.Estado_PersonalDAO;
import Controlador.HorariosDAO;
import Controlador.HistorialCambiosDAO;
import Controlador.MantenimientoDAO;
import Controlador.PersonalDAO;
import Controlador.Programacion_PersonalDAO;
import Controlador.ProveedoresDAO;
import Controlador.RolesDAO;
import Modelo.Activos;
import Modelo.Asignaciones;
import Modelo.Categorias;
import Modelo.Dias;
import Modelo.Documento;
import Modelo.Estado_Activo;
import Modelo.Estado_Personal;
import Modelo.Horarios;
import Modelo.Mantenimiento;
import Modelo.Personal;
import Modelo.Programacion_Personal;
import Modelo.Proveedores;
import Modelo.Roles;
import Seguridad.AuditoriaContexto;
import Servicio.NormalizadorTexto;
import Servicio.ValidacionActivo;
import Servicio.ValidacionPersonal;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/GuardarCatalogo", "/EditarCatalogo", "/EliminarCatalogo"})
public class CatalogoCrud extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!validarAdmin(request, response)) {
            return;
        }

        String ruta = request.getServletPath();
        String tipo = normalizarTipo(request.getParameter("tipo"));

        if (tipo.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Configuracion");
            return;
        }

        if ("/EliminarCatalogo".equals(ruta)) {
            // Las bajas y anulaciones se realizan solamente por POST.
            response.sendRedirect(request.getContextPath() + "/GestionCatalogo?tipo=" + tipo);
            return;
        }

        request.setAttribute("tipo", tipo);
        request.setAttribute("titulo", titulo(tipo));
        request.setAttribute("modo", "/EditarCatalogo".equals(ruta) ? "editar" : "guardar");
        request.setAttribute("origen", origen(request, tipo));
        cargarOpciones(request);

        if ("/EditarCatalogo".equals(ruta)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                if ("asignaciones".equals(tipo) && !new AsignacionesDAO().estaDisponibleParaEdicion(id)) {
                    response.sendRedirect(conParametro(destinoGestion(request, tipo), "error=asignacion_no_disponible"));
                    return;
                }
                cargarRegistro(request, tipo, id);
            } catch (NumberFormatException e) {
                response.sendRedirect(conParametro(destinoGestion(request, tipo), "error=id"));
                return;
            }
        }

        request.getRequestDispatcher("/Vista/FormCatalogo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!validarAdmin(request, response)) {
            return;
        }

        String ruta = request.getServletPath();
        String tipo = normalizarTipo(request.getParameter("tipo"));
        String destino = destinoGestion(request, tipo);
        boolean ok;
        Personal usuario = (Personal) request.getSession(false).getAttribute("usuarioLogueado");
        String motivo = request.getParameter("motivo");

        if ("/EliminarCatalogo".equals(ruta)) {
            if (requiereMotivo(tipo) && esVacio(motivo)) {
                response.sendRedirect(conParametro(destino, "error=motivo"));
                return;
            }
            AuditoriaContexto.establecer(usuario, motivo);
            try {
                eliminar(request, response, tipo);
            } finally {
                AuditoriaContexto.limpiar();
            }
            return;
        }

        if ("/EditarCatalogo".equals(ruta) && requiereMotivo(tipo) && esVacio(motivo)) {
            response.sendRedirect(conParametro(destino, "error=motivo"));
            return;
        }

        try {
            String errorRegla = validarReglaNegocio(request, ruta, tipo);
            if (errorRegla != null) {
                response.sendRedirect(conParametro(destino, "error=" + errorRegla));
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(conParametro(destino, "error=formato"));
            return;
        }

        AuditoriaContexto.establecer(usuario, motivo);
        try {
            if ("/EditarCatalogo".equals(ruta)) {
                ok = actualizar(request, tipo);
                response.sendRedirect(conParametro(destino, ok ? "actualizado=1" : "error=actualizar"));
            } else {
                ok = insertar(request, tipo);
                response.sendRedirect(conParametro(destino, ok ? "guardado=1" : "error=guardar"));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(conParametro(destino, "error=formato"));
        } finally {
            AuditoriaContexto.limpiar();
        }
    }

    private boolean validarAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return false;
        }

        Personal usuario = (Personal) session.getAttribute("usuarioLogueado");

        if (!usuario.esAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/VerPerfil");
            return false;
        }

        return true;
    }

    private String origen(HttpServletRequest request, String tipo) {
        if (!"asignaciones".equals(tipo)) return "configuracion";
        String origenFormulario = request.getParameter("origen");
        if ("movimientos".equals(origenFormulario) || "configuracion".equals(origenFormulario)) {
            request.getSession().setAttribute("origenAsignaciones", origenFormulario);
            return origenFormulario;
        }
        Object origenGuardado = request.getSession().getAttribute("origenAsignaciones");
        return "movimientos".equals(origenGuardado) ? "movimientos" : "configuracion";
    }

    private String destinoGestion(HttpServletRequest request, String tipo) {
        if ("asignaciones".equals(tipo) && "movimientos".equals(origen(request, tipo))) {
            return request.getContextPath() + "/GestionarAsignaciones";
        }
        return request.getContextPath() + "/GestionCatalogo?tipo=" + tipo;
    }

    private String conParametro(String destino, String parametro) {
        return destino + (destino.contains("?") ? "&" : "?") + parametro;
    }

    private String normalizarTipo(String tipo) {
        return tipo == null ? "" : tipo.trim();
    }

    private boolean requiereMotivo(String tipo) {
        return "activos".equals(tipo) || "personal".equals(tipo)
                || "asignaciones".equals(tipo) || "mantenimiento".equals(tipo)
                || "proveedores".equals(tipo);
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String validarReglaNegocio(HttpServletRequest request, String ruta, String tipo) {
        boolean edicion = "/EditarCatalogo".equals(ruta);
        if ("proveedores".equals(tipo)) {
            if (!texto(request, "nombre", 3, 60) || !digitos(request, "telefono", 7, 15)
                    || !textoDireccion(request, "direccion") || !correoCorto(request)) return "datos_proveedor";
        }
        if ("horarios".equals(tipo)) {
            String entrada = request.getParameter("horaIngreso"), salida = request.getParameter("horaSalida");
            if (!texto(request, "descripcion", 3, 60) || esVacio(entrada) || esVacio(salida) || salida.compareTo(entrada) <= 0) return "datos_horario";
        }
        if ("mantenimiento".equals(tipo)) {
            if (!fechaNoFutura(request.getParameter("fechaMante")) || !numeroPositivo(request.getParameter("costo"))
                    || !texto(request, "descripcion", 3, 200)) return "datos_mantenimiento";
        }
        if ("asignaciones".equals(tipo)) {
            if (!fechaNoFutura(request.getParameter("fechaAsignacion")) || fechaAnterior(request.getParameter("fechaDevolucion"), request.getParameter("fechaAsignacion"))
                    || !textoOpcional(request, "observaciones", 200)) return "datos_asignacion";
        }
        if ("programacionPersonal".equals(tipo)) {
            if (!texto(request, "descripcion", 3, 100) || !fechaValida(request.getParameter("fechaDesde"))
                    || fechaAnterior(request.getParameter("fechaHasta"), request.getParameter("fechaDesde"))) return "datos_programacion";
        }
        if ("roles".equals(tipo) || "documentos".equals(tipo) || "categorias".equals(tipo)
                || "estadoPersonal".equals(tipo) || "estadoActivo".equals(tipo) || "dias".equals(tipo)) {
            if (!texto(request, "descripcion", 3, 60)) return "datos_catalogo";
        }
        if ("personal".equals(tipo)) {
            int id = edicion ? Integer.parseInt(request.getParameter("id")) : 0;
            if (edicion && !new PersonalDAO().estaActivo(id)) return "personal_retirado";
            Personal personal = personalDesdeRequest(request, id);
            Documento documento = new DocumentoDAO().consultar(personal.getDocumento_id_documento());
            ValidacionPersonal.normalizar(personal);
            boolean validarClave = !edicion || !esVacio(personal.getClave());
            String error = ValidacionPersonal.validar(personal,
                    documento == null ? null : documento.getDescripcion_doc(), validarClave);
            if (error != null) return "datos_personal";
        }
        if ("activos".equals(tipo)) {
            Activos activo = activoDesdeRequest(request, edicion ? Integer.parseInt(request.getParameter("id")) : 0);
            if (ValidacionActivo.normalizarYValidar(activo) != null) return "datos_activo";
            int id = edicion ? Integer.parseInt(request.getParameter("id")) : 0;
            if (edicion && !new ActivosDAO().estaActivo(id)) {
                return "activo_retirado";
            }
            return new ActivosDAO().codigoDisponible(request.getParameter("codigoAct"), id) ? null : "activo_duplicado";
        }
        if ("asignaciones".equals(tipo)) {
            if (edicion) {
                int idAsignacion = Integer.parseInt(request.getParameter("id"));
                AsignacionesDAO dao = new AsignacionesDAO();
                Asignaciones actual = dao.consultar(idAsignacion);
                if (actual == null || (actual.getFecha_devolucion() != null
                        && !actual.getFecha_devolucion().trim().isEmpty())) {
                    return "asignacion_no_disponible";
                }
                return dao.validarEdicionAsignacion(
                        idAsignacion,
                        Integer.parseInt(request.getParameter("personalId")),
                        Integer.parseInt(request.getParameter("activoId")));
            }
            return new AsignacionesDAO().validarNuevaAsignacion(
                    Integer.parseInt(request.getParameter("personalId")),
                    Integer.parseInt(request.getParameter("activoId")));
        }
        return null;
    }

    private boolean texto(HttpServletRequest r, String campo, int minimo, int maximo) {
        String valor = r.getParameter(campo); return valor != null && valor.trim().length() >= minimo && valor.trim().length() <= maximo;
    }
    private boolean textoOpcional(HttpServletRequest r, String campo, int maximo) { String v=r.getParameter(campo); return v == null || v.trim().length() <= maximo; }
    private boolean digitos(HttpServletRequest r, String campo, int minimo, int maximo) { String v=r.getParameter(campo); return v != null && v.matches("\\d{"+minimo+","+maximo+"}"); }
    private boolean textoDireccion(HttpServletRequest r, String campo) { String v=r.getParameter(campo); return v != null && v.matches("[\\p{L}0-9#.,/\\- ]{5,40}"); }
    private boolean correoCorto(HttpServletRequest r) { String v=r.getParameter("email"); return v != null && v.length() >= 15 && v.length() <= 20 && v.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$"); }
    private boolean numeroPositivo(String valor) { try { return Double.parseDouble(valor) > 0; } catch (Exception e) { return false; } }
    private boolean fechaValida(String valor) { try { LocalDate.parse(valor); return true; } catch (DateTimeParseException | NullPointerException e) { return false; } }
    private boolean fechaNoFutura(String valor) { try { return !LocalDate.parse(valor).isAfter(LocalDate.now()); } catch (DateTimeParseException | NullPointerException e) { return false; } }
    private boolean fechaAnterior(String finalFecha, String inicial) { return finalFecha != null && !finalFecha.trim().isEmpty() && (!fechaValida(finalFecha) || finalFecha.compareTo(inicial) < 0); }

    private String titulo(String tipo) {
        switch (tipo) {
            case "roles":
                return "Roles";
            case "documentos":
                return "Documentos";
            case "categorias":
                return "Categorias";
            case "estadoPersonal":
                return "Estado Personal";
            case "estadoActivo":
                return "Estado Activo";
            case "proveedores":
                return "Proveedores";
            case "horarios":
                return "Horarios";
            case "dias":
                return "Dias";
            case "mantenimiento":
                return "Mantenimiento";
            case "personal":
                return "Personal";
            case "activos":
                return "Activos";
            case "asignaciones":
                return "Asignaciones";
            case "programacionPersonal":
                return "Programacion Personal";
            default:
                return "Catalogo";
        }
    }

    private void cargarRegistro(HttpServletRequest request, String tipo, int id) {
        request.setAttribute("id", id);

        switch (tipo) {
            case "roles":
                Roles rol = new RolesDAO().consultar(id);
                request.setAttribute("descripcion", rol != null ? rol.getDescripcion_roles() : "");
                request.setAttribute("tipoAcceso", rol != null ? rol.getTipo_acceso() : "");
                break;
            case "documentos":
                Documento doc = new DocumentoDAO().consultar(id);
                request.setAttribute("descripcion", doc != null ? doc.getDescripcion_doc() : "");
                break;
            case "categorias":
                Categorias cat = new CategoriasDAO().consultar(id);
                request.setAttribute("descripcion", cat != null ? cat.getDescripcionCategoria() : "");
                break;
            case "estadoPersonal":
                Estado_Personal ep = new Estado_PersonalDAO().consultar(id);
                request.setAttribute("descripcion", ep != null ? ep.getDescripcion_estado() : "");
                break;
            case "estadoActivo":
                Estado_Activo ea = new Estado_ActivoDAO().consultar(id);
                request.setAttribute("descripcion", ea != null ? ea.getDescripcion_activo() : "");
                break;
            case "dias":
                Dias dia = new DiasDAO().consultar(id);
                request.setAttribute("descripcion", dia != null ? dia.getDescripcionDias() : "");
                break;
            case "proveedores":
                Proveedores p = new ProveedoresDAO().consultar(id);
                request.setAttribute("nombre", p != null ? p.getNombre() : "");
                request.setAttribute("telefono", p != null ? p.getTelefono() : "");
                request.setAttribute("direccion", p != null ? p.getDireccion() : "");
                request.setAttribute("email", p != null ? p.getEmail() : "");
                break;
            case "horarios":
                Horarios h = new HorariosDAO().consultarHorario(id);
                request.setAttribute("descripcion", h != null ? h.getDescripcion() : "");
                request.setAttribute("horaIngreso", h != null ? h.getHora_ingreso() : "");
                request.setAttribute("horaSalida", h != null ? h.getHora_salida() : "");
                break;
            case "mantenimiento":
                Mantenimiento m = new MantenimientoDAO().consultarMantenimiento(id);
                request.setAttribute("fechaMante", m != null ? m.getFecha_mante() : "");
                request.setAttribute("costo", m != null ? m.getCosto() : "");
                request.setAttribute("descripcion", m != null ? m.getDescripcion() : "");
                request.setAttribute("activoId", m != null ? m.getActivos_id_activos() : "");
                request.setAttribute("proveedorId", m != null ? m.getProveedores_idProveedores() : "");
                break;
            case "personal":
                Personal per = new PersonalDAO().consultarPorId(id);
                request.setAttribute("nombre", per != null ? per.getNombre() : "");
                request.setAttribute("apellidos", per != null ? per.getApellidos() : "");
                request.setAttribute("identificacion", per != null ? per.getIdentificacion() : "");
                request.setAttribute("email", per != null ? per.getEmail() : "");
                request.setAttribute("telefono", per != null ? per.getTelefono() : "");
                request.setAttribute("direccion", per != null ? per.getDireccion() : "");
                request.setAttribute("clave", "");
                request.setAttribute("observaciones", per != null ? per.getObservaciones() : "");
                request.setAttribute("puedeAcceder", per != null && per.isPuede_acceder());
                request.setAttribute("fechaContratacion", per != null ? per.getFecha_contratacion() : "");
                request.setAttribute("documentoId", per != null ? per.getDocumento_id_documento() : "");
                request.setAttribute("rolId", per != null ? per.getRoles_idroles() : "");
                request.setAttribute("estadoPersonalId", per != null ? per.getEstado_Personal_id_estado() : "");
                request.setAttribute("esAdministradorMaestro", per != null && per.getIdPersonal() == 3);
                break;
            case "activos":
                Activos act = new ActivosDAO().consultar(id);
                request.setAttribute("codigoAct", act != null ? act.getCodigo_act() : "");
                request.setAttribute("nombreActivo", act != null ? act.getNombre_activos() : "");
                request.setAttribute("valor", act != null ? act.getValor() : "");
                request.setAttribute("fechaAdquma", act != null ? act.getFecha_adquma() : "");
                request.setAttribute("vidaUtil", act != null ? act.getVida_util() : "");
                request.setAttribute("descripcion", act != null ? act.getDescripcion() : "");
                request.setAttribute("estadoActivoId", act != null ? act.getEstado_Activo_idEstado_Activo() : "");
                request.setAttribute("categoriaId", act != null ? act.getCategorias_idCategorias() : "");
                request.setAttribute("proveedorId", act != null ? act.getProveedores_idProveedores() : "");
                break;
            case "asignaciones":
                Asignaciones asig = new AsignacionesDAO().consultar(id);
                request.setAttribute("fechaAsignacion", asig != null ? asig.getFecha_asignacion() : "");
                request.setAttribute("fechaDevolucion", asig != null ? asig.getFecha_devolucion() : "");
                request.setAttribute("observaciones", asig != null ? asig.getObservaciones() : "");
                request.setAttribute("personalId", asig != null ? asig.getPersonal_id_personal() : "");
                request.setAttribute("activoId", asig != null ? asig.getActivos_id_activos() : "");
                break;
            case "programacionPersonal":
                Programacion_Personal prog = new Programacion_PersonalDAO().consultar(id);
                request.setAttribute("descripcion", prog != null ? prog.getDescripcion_programacion() : "");
                request.setAttribute("fechaDesde", prog != null ? prog.getFecha_desde() : "");
                request.setAttribute("fechaHasta", prog != null ? prog.getFecha_hasta() : "");
                request.setAttribute("diaId", prog != null ? prog.getDias_idDias() : "");
                request.setAttribute("personalId", prog != null ? prog.getPersonal_id_personal() : "");
                request.setAttribute("horarioId", prog != null ? prog.getHorarios_id_horarios() : "");
                break;
            default:
                break;
        }
        String entidad = entidadHistorial(tipo);
        if (!entidad.isEmpty()) {
            request.setAttribute("historial", new HistorialCambiosDAO().listarPorRegistro(entidad, id));
        }
    }

    private String entidadHistorial(String tipo) {
        switch (tipo) {
            case "activos": return "Activos";
            case "personal": return "Personal";
            case "asignaciones": return "Asignaciones";
            case "mantenimiento": return "Mantenimiento";
            case "proveedores": return "Proveedores";
            default: return "";
        }
    }

    private void cargarOpciones(HttpServletRequest request) {
        request.setAttribute("opcionesDocumentos", new DocumentoDAO().listar());
        request.setAttribute("opcionesRoles", new RolesDAO().listar());
        request.setAttribute("opcionesEstadoPersonal", new Estado_PersonalDAO().listar());
        request.setAttribute("opcionesEstadoActivo", new Estado_ActivoDAO().listar());
        request.setAttribute("opcionesCategorias", new CategoriasDAO().listar());
        request.setAttribute("opcionesProveedores", new ProveedoresDAO().listar());
        boolean formularioAsignaciones = "asignaciones".equals(normalizarTipo(request.getParameter("tipo")));
        Integer idAsignacionActual = null;
        if (formularioAsignaciones && "/EditarCatalogo".equals(request.getServletPath())) {
            try {
                idAsignacionActual = Integer.valueOf(request.getParameter("id"));
            } catch (NumberFormatException e) {
                // La validación del identificador se conserva en doGet.
            }
        }
        request.setAttribute("opcionesActivos", formularioAsignaciones
                ? new ActivosDAO().listarDisponiblesParaAsignacion(idAsignacionActual)
                : new ActivosDAO().listar());
        request.setAttribute("opcionesPersonal", new PersonalDAO().consultarTodo());
        request.setAttribute("opcionesDias", new DiasDAO().listar());
        request.setAttribute("opcionesHorarios", new HorariosDAO().consultar());
    }

    private boolean insertar(HttpServletRequest request, String tipo) {
        switch (tipo) {
            case "roles":
                Roles rol = new Roles();
                rol.setDescripcion_roles(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                rol.setTipo_acceso(request.getParameter("tipoAcceso"));
                return new RolesDAO().insertar(rol);
            case "documentos":
                Documento doc = new Documento();
                doc.setDescripcion_doc(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new DocumentoDAO().insertar(doc);
            case "categorias":
                Categorias cat = new Categorias();
                cat.setDescripcionCategoria(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new CategoriasDAO().insertar(cat);
            case "estadoPersonal":
                Estado_Personal ep = new Estado_Personal();
                ep.setDescripcion_estado(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new Estado_PersonalDAO().insertar(ep);
            case "estadoActivo":
                Estado_Activo ea = new Estado_Activo();
                ea.setDescripcion_activo(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new Estado_ActivoDAO().insertar(ea);
            case "dias":
                Dias dia = new Dias();
                dia.setDescripcionDias(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new DiasDAO().insertar(dia);
            case "proveedores":
                return new ProveedoresDAO().insertar(proveedorDesdeRequest(request, 0));
            case "horarios":
                return new HorariosDAO().insertar(horarioDesdeRequest(request, 0));
            case "mantenimiento":
                return new MantenimientoDAO().insertar(mantenimientoDesdeRequest(request, 0));
            case "personal":
                return new PersonalDAO().insertar(personalDesdeRequest(request, 0));
            case "activos":
                return new ActivosDAO().insertar(activoDesdeRequest(request, 0));
            case "asignaciones":
                return new AsignacionesDAO().insertar(asignacionDesdeRequest(request, 0));
            case "programacionPersonal":
                return new Programacion_PersonalDAO().insertar(programacionDesdeRequest(request, 0));
            default:
                return false;
        }
    }

    private boolean actualizar(HttpServletRequest request, String tipo) {
        int id = Integer.parseInt(request.getParameter("id"));

        switch (tipo) {
            case "roles":
                Roles rol = new Roles();
                rol.setIdRoles(id);
                rol.setDescripcion_roles(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                rol.setTipo_acceso(request.getParameter("tipoAcceso"));
                return new RolesDAO().actualizar(rol);
            case "documentos":
                Documento doc = new Documento();
                doc.setId_documento(id);
                doc.setDescripcion_doc(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new DocumentoDAO().actualizar(doc);
            case "categorias":
                Categorias cat = new Categorias();
                cat.setIdCategorias(id);
                cat.setDescripcionCategoria(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new CategoriasDAO().actualizar(cat);
            case "estadoPersonal":
                Estado_Personal ep = new Estado_Personal();
                ep.setId_estado(id);
                ep.setDescripcion_estado(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new Estado_PersonalDAO().actualizar(ep);
            case "estadoActivo":
                Estado_Activo ea = new Estado_Activo();
                ea.setIdEstado_Activo(id);
                ea.setDescripcion_activo(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new Estado_ActivoDAO().actualizar(ea);
            case "dias":
                Dias dia = new Dias();
                dia.setIdDias(id);
                dia.setDescripcionDias(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
                return new DiasDAO().actualizar(dia);
            case "proveedores":
                return new ProveedoresDAO().actualizar(proveedorDesdeRequest(request, id));
            case "horarios":
                return new HorariosDAO().actualizar(horarioDesdeRequest(request, id));
            case "mantenimiento":
                return new MantenimientoDAO().actualizar(mantenimientoDesdeRequest(request, id));
            case "personal":
                Personal personal = personalDesdeRequest(request, id);
                if (personal.getClave() == null || personal.getClave().trim().isEmpty()) {
                    Personal actual = new PersonalDAO().consultarPorId(id);
                    if (actual == null) {
                        return false;
                    }
                    personal.setClave(actual.getClave());
                }
                return new PersonalDAO().actualizarPorId(personal);
            case "activos":
                return new ActivosDAO().actualizar(activoDesdeRequest(request, id));
            case "asignaciones":
                return new AsignacionesDAO().actualizar(asignacionDesdeRequest(request, id));
            case "programacionPersonal":
                return new Programacion_PersonalDAO().actualizar(programacionDesdeRequest(request, id));
            default:
                return false;
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response, String tipo)
            throws IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean ok;

            switch (tipo) {
                case "roles":
                    ok = new RolesDAO().eliminar(id);
                    break;
                case "documentos":
                    // Los tipos de documento son un catalogo base reutilizado por
                    // Personal y por las solicitudes de Contacto; el sistema no
                    // permite dar de baja este catalogo para no perder informacion
                    // referenciada por esos registros. La vista de solo lectura y
                    // edicion se conserva sin cambios.
                    ok = false;
                    break;
                case "categorias":
                    ok = new CategoriasDAO().eliminar(id);
                    break;
                case "estadoPersonal":
                    ok = new Estado_PersonalDAO().eliminar(id);
                    break;
                case "estadoActivo":
                    ok = new Estado_ActivoDAO().eliminar(id);
                    break;
                case "dias":
                    ok = new DiasDAO().eliminar(id);
                    break;
                case "proveedores":
                    ok = new ProveedoresDAO().eliminar(id);
                    break;
                case "horarios":
                    ok = new HorariosDAO().eliminar(id);
                    break;
                case "mantenimiento":
                    ok = new MantenimientoDAO().eliminar(id);
                    break;
                case "personal":
                    ok = new PersonalDAO().desactivarPorId(id);
                    break;
                case "activos":
                    ok = new ActivosDAO().eliminar(id);
                    break;
                case "asignaciones":
                    ok = new AsignacionesDAO().anularPorId(id);
                    break;
                case "programacionPersonal":
                    ok = new Programacion_PersonalDAO().eliminar(id);
                    break;
                default:
                    ok = false;
                    break;
            }

            String resultado = ("personal".equals(tipo) || "asignaciones".equals(tipo))
                    ? "&conservado=1" : "&eliminado=1";
            response.sendRedirect(request.getContextPath() + "/GestionCatalogo?tipo=" + tipo
                    + (ok ? resultado : "&error=eliminar"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/GestionCatalogo?tipo=" + tipo + "&error=id");
        }
    }

    private Proveedores proveedorDesdeRequest(HttpServletRequest request, int id) {
        Proveedores p = new Proveedores();
        p.setIdProveedores(id);
        p.setNombre(NormalizadorTexto.mayusculas(request.getParameter("nombre")));
        p.setTelefono(request.getParameter("telefono"));
        p.setDireccion(NormalizadorTexto.mayusculas(request.getParameter("direccion")));
        p.setEmail(request.getParameter("email"));
        return p;
    }

    private Horarios horarioDesdeRequest(HttpServletRequest request, int id) {
        Horarios h = new Horarios();
        h.setId_horarios(id);
        h.setDescripcion(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
        h.setHora_ingreso(request.getParameter("horaIngreso"));
        h.setHora_salida(request.getParameter("horaSalida"));
        return h;
    }

    private Mantenimiento mantenimientoDesdeRequest(HttpServletRequest request, int id) {
        Mantenimiento m = new Mantenimiento();
        m.setId_mantenimiento(id);
        m.setFecha_mante(request.getParameter("fechaMante"));
        m.setCosto(request.getParameter("costo"));
        m.setDescripcion(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
        m.setActivos_id_activos(Integer.parseInt(request.getParameter("activoId")));
        m.setProveedores_idProveedores(Integer.parseInt(request.getParameter("proveedorId")));
        return m;
    }

    private Personal personalDesdeRequest(HttpServletRequest request, int id) {
        Personal p = new Personal();
        p.setIdPersonal(id);
        p.setNombre(request.getParameter("nombre"));
        p.setApellidos(request.getParameter("apellidos"));
        p.setIdentificacion(request.getParameter("identificacion"));
        p.setEmail(request.getParameter("email"));
        p.setTelefono(request.getParameter("telefono"));
        p.setDireccion(request.getParameter("direccion"));
        p.setClave(request.getParameter("clave"));
        p.setObservaciones(request.getParameter("observaciones"));
        p.setPuede_acceder("on".equals(request.getParameter("puedeAcceder")));
        p.setFecha_contratacion(request.getParameter("fechaContratacion"));
        p.setDocumento_id_documento(Integer.parseInt(request.getParameter("documentoId")));
        p.setRoles_idroles(Integer.parseInt(request.getParameter("rolId")));
        p.setEstado_Personal_id_estado(Integer.parseInt(request.getParameter("estadoPersonalId")));
        return p;
    }

    private Activos activoDesdeRequest(HttpServletRequest request, int id) {
        Activos a = new Activos();
        a.setId_activos(id);
        a.setCodigo_act(request.getParameter("codigoAct"));
        a.setNombre_activos(request.getParameter("nombreActivo"));
        a.setValor(request.getParameter("valor"));
        a.setFecha_adquma(request.getParameter("fechaAdquma"));
        // La devolución se registra exclusivamente en la asignación.
        a.setFecha_devolucion(null);
        a.setVida_util(Math.max(0, Integer.parseInt(request.getParameter("vidaUtil"))));
        a.setDescripcion(request.getParameter("descripcion"));
        a.setEstado_Activo_idEstado_Activo(Integer.parseInt(request.getParameter("estadoActivoId")));
        a.setCategorias_idCategorias(Integer.parseInt(request.getParameter("categoriaId")));
        a.setProveedores_idProveedores(Integer.parseInt(request.getParameter("proveedorId")));
        return a;
    }

    private Asignaciones asignacionDesdeRequest(HttpServletRequest request, int id) {
        Asignaciones a = new Asignaciones();
        a.setId_asignaciones(id);
        a.setFecha_asignacion(request.getParameter("fechaAsignacion"));
        a.setFecha_devolucion(request.getParameter("fechaDevolucion"));
        a.setObservaciones(NormalizadorTexto.mayusculas(request.getParameter("observaciones")));
        a.setPersonal_id_personal(Integer.parseInt(request.getParameter("personalId")));
        a.setActivos_id_activos(Integer.parseInt(request.getParameter("activoId")));
        return a;
    }

    private Programacion_Personal programacionDesdeRequest(HttpServletRequest request, int id) {
        Programacion_Personal p = new Programacion_Personal();
        p.setIdProgramacion_Personal(id);
        p.setDescripcion_programacion(NormalizadorTexto.mayusculas(request.getParameter("descripcion")));
        p.setFecha_desde(request.getParameter("fechaDesde"));
        p.setFecha_hasta(request.getParameter("fechaHasta"));
        p.setDias_idDias(Integer.parseInt(request.getParameter("diaId")));
        p.setPersonal_id_personal(Integer.parseInt(request.getParameter("personalId")));
        p.setHorarios_id_horarios(Integer.parseInt(request.getParameter("horarioId")));
        return p;
    }
}
