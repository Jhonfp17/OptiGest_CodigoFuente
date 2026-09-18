package Servlet;

import Controlador.HistorialCambiosDAO;
import Modelo.Personal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/HistorialAdministrativo")
public class HistorialAdministrativo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }
        Personal usuario = (Personal) session.getAttribute("usuarioLogueado");
        if (!usuario.esAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/VerPerfil");
            return;
        }
        String entidad = request.getParameter("entidad");
        String accion = request.getParameter("accion");
        String busqueda = request.getParameter("buscar");
        request.setAttribute("entidad", entidad == null ? "" : entidad);
        request.setAttribute("accion", accion == null ? "" : accion);
        request.setAttribute("busqueda", busqueda == null ? "" : busqueda);
        request.setAttribute("historial", new HistorialCambiosDAO().listar(entidad, accion, busqueda));
        request.getRequestDispatcher("/Vista/HistorialAdministrativoProfesional.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
