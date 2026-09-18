package Servlet;

import Modelo.Personal;
import Servicio.AutenticacionService;
import Servicio.AutenticacionService.ResultadoAutenticacion;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "InicioSesion", urlPatterns = {"/Iniciar"})
public class InicioSesion extends HttpServlet {

    private final AutenticacionService autenticacionService = new AutenticacionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String ctx = request.getContextPath();

        try {
            String identificacion = request.getParameter("Usuario");
            String passIngresada  = request.getParameter("pass");
            ResultadoAutenticacion resultado = autenticacionService.autenticar(identificacion, passIngresada);
            if (!resultado.esExitoso()) {
                request.setAttribute("mensaje", mensajePara(resultado));
                request.getRequestDispatcher("/Vista/InicioSesion.jsp")
                        .forward(request, response);
                return;
            }

            Personal p = resultado.getPersonal();
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", p);
            session.setAttribute("rolAcceso", p.getRoles_idroles());
            if (p.isDebeCambiarClave()) {
                response.sendRedirect(ctx + "/VerPerfil?cambioObligatorio=1");
                return;
            }
            if (p.esAdministrador()) {
                response.sendRedirect(ctx + "/PanelControl");
            } else {
                response.sendRedirect(ctx + "/VerPerfil");
            }

        } catch (Exception e) {
            System.out.println("Error en InicioSesion: " + e.getMessage());
            request.setAttribute("mensaje", "Ocurrio un error al iniciar sesion.");
            request.getRequestDispatcher("/Vista/InicioSesion.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
    }

    private String mensajePara(ResultadoAutenticacion resultado) {
        switch (resultado.getEstado()) {
            case CAMPOS_INCOMPLETOS:
                return "Por favor completa todos los campos.";
            case USUARIO_NO_ENCONTRADO:
                return "Usuario no encontrado.";
            case SIN_PERMISO:
                return "No tienes acceso al sistema. Comunícate con el administrador.";
            case CLAVE_INCORRECTA:
                return "Contrasena incorrecta.";
            case CLAVE_TEMPORAL_VENCIDA:
                return "La contraseña temporal venció. Solicita al administrador que genere una nueva.";
            default:
                return "Ocurrio un error al iniciar sesion.";
        }
    }
}
