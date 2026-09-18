package Servlet;

import Controlador.PersonalDAO;
import Modelo.Personal;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Permite al administrador renovar una clave temporal vencida u olvidada. */
@WebServlet("/RegenerarClaveTemporal")
public class RegenerarClaveTemporal extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sesion = req.getSession(false);
        if (sesion == null || !(sesion.getAttribute("usuarioLogueado") instanceof Personal)
                || !((Personal) sesion.getAttribute("usuarioLogueado")).esAdministrador()) {
            resp.sendRedirect(req.getContextPath() + "/Vista/InicioSesion.jsp"); return;
        }
        try {
            int id = Integer.parseInt(req.getParameter("personalId"));
            String clave = generarClave();
            LocalDateTime expiraEn = LocalDateTime.now().plusMinutes(30);
            if (new PersonalDAO().actualizarClaveTemporal(id, clave, expiraEn)) {
                sesion.setAttribute("claveGenerada", clave);
                sesion.setAttribute("claveGeneradaExpiraEn", expiraEn.toString());
                sesion.setAttribute("claveGeneradaPersonalId", id);
                resp.sendRedirect(req.getContextPath() + "/GestionContacto?claveRenovada=1");
            } else resp.sendRedirect(req.getContextPath() + "/GestionContacto?error=1");
        } catch (NumberFormatException e) { resp.sendRedirect(req.getContextPath() + "/GestionContacto?error=1"); }
    }
    private String generarClave() {
        String caracteres = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
        SecureRandom aleatorio = new SecureRandom(); StringBuilder clave = new StringBuilder("Act-");
        for (int i = 0; i < 16; i++) clave.append(caracteres.charAt(aleatorio.nextInt(caracteres.length())));
        return clave.toString();
    }
}
