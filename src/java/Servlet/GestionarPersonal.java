
package Servlet;

import Controlador.Estado_PersonalDAO;
import Controlador.PersonalDAO;
import Controlador.RolesDAO;
import Modelo.Estado_Personal;
import Modelo.Personal;
import Modelo.Roles;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/GestionarPersonal")
public class GestionarPersonal extends HttpServlet {

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
        Object claveGenerada = session.getAttribute("claveGenerada");
        if (claveGenerada != null) {
            request.setAttribute("claveGenerada", claveGenerada);
            session.removeAttribute("claveGenerada");
        }
        request.setAttribute("listaPersonal", new PersonalDAO().consultarTodo());
        request.setAttribute("listaPersonalRetirado", new PersonalDAO().listarInactivos());
        request.setAttribute("rolesMap", mapaRoles(new RolesDAO().listar()));
        request.setAttribute("estadoPersonalMap", mapaEstadoPersonal(new Estado_PersonalDAO().listar()));
        request.getRequestDispatcher("/Vista/GestionPersonal.jsp")
               .forward(request, response);
    }

    private Map<Integer, String> mapaRoles(List<Roles> roles) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Roles rol : roles) {
            mapa.put(rol.getIdRoles(), rol.getDescripcion_roles());
        }
        return mapa;
    }

    private Map<Integer, String> mapaEstadoPersonal(List<Estado_Personal> estados) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Estado_Personal estado : estados) {
            mapa.put(estado.getId_estado(), estado.getDescripcion_estado());
        }
        return mapa;
    }
}
