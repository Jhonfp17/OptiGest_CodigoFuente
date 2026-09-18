package Servlet;

import Controlador.ContactoDAO;
import Modelo.Personal;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/GestionContacto")
public class GestionContacto extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdministrador(req, resp)) return;
        HttpSession sesion = req.getSession(false);
        Object claveGenerada = sesion == null ? null : sesion.getAttribute("claveGenerada");
        if (claveGenerada != null) {
            req.setAttribute("claveGenerada", claveGenerada);
            req.setAttribute("claveGeneradaExpiraEn", sesion.getAttribute("claveGeneradaExpiraEn"));
            req.setAttribute("claveGeneradaPersonalId", sesion.getAttribute("claveGeneradaPersonalId"));
            sesion.removeAttribute("claveGenerada");
            sesion.removeAttribute("claveGeneradaExpiraEn");
            sesion.removeAttribute("claveGeneradaPersonalId");
        }
        req.setAttribute("mensajes", new ContactoDAO().listar());
        req.setAttribute("mensajesArchivados", new ContactoDAO().listarArchivadas());
        req.getRequestDispatcher("/Vista/GestionContacto.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!esAdministrador(req, resp)) return;
        try {
            boolean ok = new ContactoDAO().marcarAtendido(Long.parseLong(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/GestionContacto?" + (ok ? "atendido=1" : "error=1"));
        } catch (NumberFormatException e) { resp.sendRedirect(req.getContextPath() + "/GestionContacto?error=1"); }
    }
    private boolean esAdministrador(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("usuarioLogueado") == null) { resp.sendRedirect(req.getContextPath() + "/Vista/InicioSesion.jsp"); return false; }
        if (!((Personal) s.getAttribute("usuarioLogueado")).esAdministrador()) { resp.sendRedirect(req.getContextPath() + "/VerPerfil"); return false; }
        return true;
    }
}
