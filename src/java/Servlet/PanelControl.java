package Servlet;

import Controlador.ActivosDAO;
import Controlador.AsignacionesDAO;
import Controlador.PersonalDAO;
import Controlador.ContactoDAO;
import Modelo.Activos;
import Modelo.Asignaciones;
import Modelo.Personal;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PanelControl")
public class PanelControl extends HttpServlet {

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
        AsignacionesDAO asignacionesDAO = new AsignacionesDAO();
        PersonalDAO personalDAO = new PersonalDAO();
        ActivosDAO activosDAO = new ActivosDAO();

        List<Asignaciones> asignaciones = asignacionesDAO.listar();
        List<Personal> listaPersonal = personalDAO.consultarTodo();
        List<Activos> activos = activosDAO.listar();

        request.setAttribute("asignaciones", asignaciones);
        request.setAttribute("listaPersonal", listaPersonal);
        request.setAttribute("totalActivos",
                activos != null ? activos.size() : 0);
        request.setAttribute("personalMap", mapaPersonal(listaPersonal));
        request.setAttribute("activosMap", mapaActivos(activos));
        request.setAttribute("solicitudesPendientes", new ContactoDAO().contarPendientes());
        String alias = new ContactoDAO().consultarUsuarioDeseadoPorPersonal(usuario.getIdPersonal());
        String nombreVisible = alias == null || alias.trim().isEmpty()
                ? usuario.getNombre() + " " + usuario.getApellidos() : alias;
        request.setAttribute("nombreVisible", nombreVisible.toUpperCase(Locale.forLanguageTag("es-CO")));

        request.getRequestDispatcher("/Vista/PanelControlActivosYPersonal.jsp")
                .forward(request, response);
    }

    private Map<Integer, String> mapaPersonal(List<Personal> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        if (lista != null) {
            for (Personal item : lista) {
                mapa.put(item.getIdPersonal(), item.getNombre() + " " + item.getApellidos());
            }
        }
        return mapa;
    }

    private Map<Integer, String> mapaActivos(List<Activos> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        if (lista != null) {
            for (Activos item : lista) {
                mapa.put(item.getId_activos(), item.getNombre_activos());
            }
        }
        return mapa;
    }
}
