package Servlet;

import Controlador.AsignacionesDAO;
import Controlador.Estado_PersonalDAO;
import Controlador.PersonalDAO;
import Modelo.AsignacionConsulta;
import Modelo.Estado_Personal;
import Modelo.Personal;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
    
@WebServlet("/VerPerfil")
public class VerPerfil extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("usuarioLogueado") == null) {

            response.sendRedirect(
                request.getContextPath()
                + "/Vista/InicioSesion.jsp");
            return;
        }

        try {

            Personal usuario = (Personal) session.getAttribute("usuarioLogueado");
            String idParam = request.getParameter("id");
            int id = idParam == null || idParam.trim().isEmpty()
                    ? usuario.getIdPersonal()
                    : Integer.parseInt(idParam);

            if (!usuario.esAdministrador() && id != usuario.getIdPersonal()) {
                id = usuario.getIdPersonal();
            }

            PersonalDAO dao = new PersonalDAO();
            Personal per = dao.consultarPorId(id);

            if (per == null) {
                response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
                return;
            }

            request.setAttribute("personal", per);
            Estado_Personal estadoPersonal = new Estado_PersonalDAO()
                    .consultar(per.getEstado_Personal_id_estado());
            request.setAttribute("estadoPersonal", estadoPersonal);

            List<AsignacionConsulta> activosAsignados = new AsignacionesDAO().listarActivasPorPersonal(per.getIdPersonal());
            request.setAttribute("activosAsignados", activosAsignados);

            request.getRequestDispatcher(
                    "/Vista/PerfilUsuario.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
        }
    }
}
