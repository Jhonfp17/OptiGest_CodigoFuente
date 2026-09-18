package Servlet;

import Controlador.AsignacionesDAO;
import Controlador.ActivosDAO;
import Modelo.AsignacionConsulta;
import Modelo.Personal;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AsignacionesPersonal")
public class AsignacionesPersonal extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        String tipoBusqueda = limpiar(request.getParameter("tipoBusqueda"));
        String valorBusqueda = limpiar(request.getParameter("valorBusqueda"));
        String documento = limpiar(request.getParameter("documento"));

        if (tipoBusqueda == null) {
            tipoBusqueda = "documento";
        }
        if (valorBusqueda == null) {
            valorBusqueda = documento;
        }

        request.setAttribute("tipoBusqueda", tipoBusqueda);
        request.setAttribute("valorBusqueda", valorBusqueda);
        request.setAttribute("fechaActual", LocalDate.now().toString());
        request.setAttribute("activosBusqueda", new ActivosDAO().listar());

        AsignacionesDAO asignacionesDAO = new AsignacionesDAO();
        List<AsignacionConsulta> asignaciones;

        if (valorBusqueda != null && !valorBusqueda.isEmpty()) {
            asignaciones = asignacionesDAO
                    .consultarPorFiltroYRango(tipoBusqueda, valorBusqueda, null, null, 0);
            request.setAttribute("busquedaRealizada", true);
        } else {
            asignaciones = asignacionesDAO.listarUltimasConsulta(0);
            request.setAttribute("busquedaRealizada", false);
        }

        request.setAttribute("asignacionesConsulta", asignaciones);

        request.getRequestDispatcher("/Vista/AsignacionesPersonal.jsp")
                .forward(request, response);
    }

    private String limpiar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}

