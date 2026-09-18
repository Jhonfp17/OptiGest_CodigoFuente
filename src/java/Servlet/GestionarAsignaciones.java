package Servlet;

import Controlador.AsignacionesDAO;
import Modelo.AsignacionConsulta;
import Modelo.Personal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/** Vista operativa de asignaciones para el Gestor de Movimientos. */
@WebServlet("/GestionarAsignaciones")
public class GestionarAsignaciones extends HttpServlet {

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

        session.setAttribute("origenAsignaciones", "movimientos");

        List<AsignacionConsulta> asignaciones = new AsignacionesDAO().listarUltimasConsulta(0);
        request.setAttribute("asignaciones", asignaciones);
        request.setAttribute("fechaActual", LocalDate.now().toString());
        request.getRequestDispatcher("/Vista/GestionAsignaciones.jsp").forward(request, response);
    }
}
