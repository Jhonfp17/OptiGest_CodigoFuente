package Servlet;

import Controlador.PersonalDAO;
import Modelo.Personal;
import Seguridad.AuditoriaContexto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/EliminarPersonal")
public class EliminarPersonal extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/GestionarPersonal");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
            String motivo = request.getParameter("motivo");
            if (motivo == null || motivo.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/GestionarPersonal?error=motivo");
                return;
            }
            PersonalDAO dao = new PersonalDAO();
            if (dao.tieneAsignacionesActivas(id)) {
                response.sendRedirect(request.getContextPath() + "/GestionarPersonal?bloqueo=asignaciones_activas");
                return;
            }
            AuditoriaContexto.establecer(admin, motivo);
            boolean desactivado;
            try {
                desactivado = dao.desactivarPorId(id);
            } finally {
                AuditoriaContexto.limpiar();
            }

            if (desactivado) {
                response.sendRedirect(request.getContextPath() + "/GestionarPersonal?desactivado=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/GestionarPersonal?error=desactivar");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/GestionarPersonal?error=id");
        }
    }
}
