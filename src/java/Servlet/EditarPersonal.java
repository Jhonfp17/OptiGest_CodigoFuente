package Servlet;

import Controlador.DocumentoDAO;
import Controlador.Estado_PersonalDAO;
import Controlador.HistorialCambiosDAO;
import Controlador.PersonalDAO;
import Controlador.RolesDAO;
import Modelo.Personal;
import Seguridad.AuditoriaContexto;
import Servicio.ValidacionPersonal;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/EditarPersonal")
public class EditarPersonal extends HttpServlet {

    private void cargarListas(HttpServletRequest request) {
        request.setAttribute("documentos",     new DocumentoDAO().listar());
        request.setAttribute("roles",          new RolesDAO().listar());
        request.setAttribute("estadosPersonal", new Estado_PersonalDAO().listar());
    }
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
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            PersonalDAO dao = new PersonalDAO();
            Personal per = dao.consultarPorId(id);

            if (per == null || !dao.estaActivo(id)) {
                response.sendRedirect(request.getContextPath() + "/GestionarPersonal?error=personal_retirado");
                return;
            }
            request.setAttribute("personal", per);
            request.setAttribute("esAdministradorMaestro", per.getIdPersonal() == 3);
            cargarListas(request);
            request.setAttribute("historial", new HistorialCambiosDAO().listarPorRegistro("Personal", id));

            request.getRequestDispatcher("/Vista/EditarPersonal.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/GestionarPersonal");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

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
        String motivo = request.getParameter("motivo");
        if (motivo == null || motivo.trim().isEmpty()) {
            request.setAttribute("mensaje", "Indica el motivo del cambio para conservar la trazabilidad.");
            try {
                int id = Integer.parseInt(request.getParameter("idPersonal"));
                Personal actual = new PersonalDAO().consultarPorId(id);
                request.setAttribute("personal", actual);
                request.setAttribute("esAdministradorMaestro", actual != null && actual.getIdPersonal() == 3);
                request.setAttribute("historial", new HistorialCambiosDAO().listarPorRegistro("Personal", id));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/GestionarPersonal");
                return;
            }
            cargarListas(request);
            request.getRequestDispatcher("/Vista/EditarPersonal.jsp").forward(request, response);
            return;
        }

        try {
            Personal per = new Personal();

            per.setIdPersonal(Integer.parseInt(request.getParameter("idPersonal")));
            PersonalDAO dao = new PersonalDAO();
            if (!dao.estaActivo(per.getIdPersonal())) {
                response.sendRedirect(request.getContextPath() + "/GestionarPersonal?error=personal_retirado");
                return;
            }
            per.setIdentificacion(request.getParameter("identificacion"));
            per.setNombre(request.getParameter("nombre"));
            per.setApellidos(request.getParameter("apellidos"));
            per.setEmail(request.getParameter("email"));
            per.setTelefono(request.getParameter("telefono"));
            per.setDireccion(request.getParameter("direccion"));
            String clave = request.getParameter("clave");
            if (clave == null || clave.trim().isEmpty()) {
                Personal actual = dao.consultarPorId(per.getIdPersonal());
                if (actual == null) {
                    response.sendRedirect(request.getContextPath() + "/GestionarPersonal");
                    return;
                }
                per.setClave(actual.getClave());
            } else {
                per.setClave(clave);
            }
            per.setObservaciones(request.getParameter("observaciones"));
            per.setFecha_contratacion(request.getParameter("fecha_contratacion"));
            per.setPuede_acceder(Integer.parseInt(request.getParameter("puede_acceder")) == 1);
            per.setDocumento_id_documento(Integer.parseInt(request.getParameter("Documento_id_documento")));
            per.setRoles_idroles(Integer.parseInt(request.getParameter("roles_idroles")));
            per.setEstado_Personal_id_estado(Integer.parseInt(request.getParameter("Estado_Personal_id_estado")));

            DocumentoDAO documentoDAO = new DocumentoDAO();
            Modelo.Documento documento = documentoDAO.consultar(per.getDocumento_id_documento());
            ValidacionPersonal.normalizar(per);
            String errorValidacion = ValidacionPersonal.validar(per,
                    documento == null ? null : documento.getDescripcion_doc(),
                    clave != null && !clave.trim().isEmpty());
            if (errorValidacion != null) {
                request.setAttribute("mensaje", errorValidacion);
                request.setAttribute("personal", per);
                request.setAttribute("esAdministradorMaestro", per.getIdPersonal() == 3);
                cargarListas(request);
                request.setAttribute("historial", new HistorialCambiosDAO().listar("Personal", String.valueOf(per.getIdPersonal())));
                request.getRequestDispatcher("/Vista/EditarPersonal.jsp").forward(request, response);
                return;
            }

            AuditoriaContexto.establecer(admin, motivo);
            boolean actualizado;
            try {
                actualizado = dao.actualizarPorId(per);
            } finally {
                AuditoriaContexto.limpiar();
            }
            if (actualizado) {
                response.sendRedirect(request.getContextPath() + "/GestionarPersonal?actualizado=1");
            } else {
                request.setAttribute("mensaje", "Error al actualizar. Verifica los datos.");
                request.setAttribute("personal", per);
                request.setAttribute("esAdministradorMaestro", per.getIdPersonal() == 3);
                cargarListas(request);
                request.setAttribute("historial", new HistorialCambiosDAO().listar("Personal", String.valueOf(per.getIdPersonal())));
                request.getRequestDispatcher("/Vista/EditarPersonal.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "Hay campos invalidos. Revisa los selects.");
            cargarListas(request);
            request.getRequestDispatcher("/Vista/EditarPersonal.jsp")
                    .forward(request, response);
        }
    }
}
