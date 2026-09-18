package Servlet;

import Controlador.PersonalDAO;
import Modelo.Personal;
import Seguridad.PasswordHasher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CambiarContrasena")
public class CambiarContrasena extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        Personal usuarioSesion = (Personal) session.getAttribute("usuarioLogueado");
        String claveActual = valor(request.getParameter("claveActual"));
        String nuevaClave = valor(request.getParameter("nuevaClave"));
        String confirmarClave = valor(request.getParameter("confirmarClave"));

        if (claveActual.isEmpty() || nuevaClave.isEmpty() || confirmarClave.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/VerPerfil?claveError=campos");
            return;
        }

        if (!nuevaClave.equals(confirmarClave)) {
            response.sendRedirect(request.getContextPath() + "/VerPerfil?claveError=confirmacion");
            return;
        }

        if (nuevaClave.length() < 8 || nuevaClave.length() > 20) {
            response.sendRedirect(request.getContextPath() + "/VerPerfil?claveError=longitud");
            return;
        }

        PersonalDAO dao = new PersonalDAO();
        Personal usuarioActual = dao.consultarPorId(usuarioSesion.getIdPersonal());

        if (usuarioActual == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        if (!PasswordHasher.verificar(claveActual, usuarioActual.getClave())) {
            response.sendRedirect(request.getContextPath() + "/VerPerfil?claveError=actual");
            return;
        }

        boolean actualizado = dao.actualizarClavePorId(usuarioActual.getIdPersonal(), nuevaClave);

        if (actualizado) {
            usuarioActual.setClave(null);
            usuarioActual.setDebeCambiarClave(false);
            session.setAttribute("usuarioLogueado", usuarioActual);
            response.sendRedirect(request.getContextPath() + "/VerPerfil?claveOk=1");
        } else {
            response.sendRedirect(request.getContextPath() + "/VerPerfil?claveError=guardar");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/VerPerfil");
    }

    private String valor(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
