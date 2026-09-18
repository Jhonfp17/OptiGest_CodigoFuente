package Servlet;

import Controlador.ActivosDAO;
import Controlador.ProveedoresDAO;
import Modelo.Activos;
import Modelo.Personal;
import Modelo.Proveedores;
import Seguridad.AuditoriaContexto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ControladorActivo", urlPatterns = {"/ControladorActivo"})
public class ControladorActivo extends HttpServlet {

    private final ActivosDAO dao = new ActivosDAO();

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

        String accion = request.getParameter("accion");

        if ("estados".equals(accion)) {
            responderEstados(response);
        } else if (accion == null || accion.equals("listar")) {
            listar(request, response);
        } else if (accion.equals("editar")) {
            editar(request, response);
        } else if (accion.equals("eliminar")) {
            // El retiro es una operacion de estado y solo se acepta por POST.
            response.sendRedirect(request.getContextPath() + "/ControladorActivo?accion=listar");
        } else {
            listar(request, response);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Activos> listaActivos = dao.listar();

        request.setAttribute("activos", listaActivos);
        request.setAttribute("activosInactivos", dao.listarInactivos());
        request.setAttribute("proveedoresMap", mapaProveedores(new ProveedoresDAO().listar()));

        request.getRequestDispatcher("/Vista/GestionActivos.jsp")
                .forward(request, response);
    }

    private Map<Integer, String> mapaProveedores(List<Proveedores> proveedores) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Proveedores proveedor : proveedores) {
            mapa.put(proveedor.getIdProveedores(), proveedor.getNombre());
        }
        return mapa;
    }

    private void responderEstados(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        StringBuilder respuesta = new StringBuilder("{");
        boolean primero = true;
        for (Activos activo : dao.listar()) {
            if (!primero) respuesta.append(',');
            respuesta.append('"').append(activo.getId_activos()).append("\":\"")
                    .append(escaparJson(activo.getEstadoActual())).append('"');
            primero = false;
        }
        respuesta.append('}');
        response.getWriter().write(respuesta.toString());
    }

    private String escaparJson(String valor) {
        return valor == null ? "" : valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            response.sendRedirect(
                    request.getContextPath()
                    + "/EditarActivo?id="
                    + id);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/ControladorActivo?accion=listar");
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        boolean eliminado = false;
        String bloqueo = null;

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            String motivo = request.getParameter("motivo");
            if (motivo == null || motivo.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/ControladorActivo?accion=listar&error=motivo");
                return;
            }
            bloqueo = dao.motivoBloqueoBaja(id);
            if (bloqueo == null) {
                Personal admin = (Personal) request.getSession(false).getAttribute("usuarioLogueado");
                AuditoriaContexto.establecer(admin, motivo);
                try {
                    eliminado = dao.eliminar(id);
                } finally {
                    AuditoriaContexto.limpiar();
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("ID de activo inválido.");
        }

        String destino = request.getContextPath() + "/ControladorActivo?accion=listar";
        if (bloqueo != null) {
            destino += "&bloqueo=" + bloqueo;
        } else if (eliminado) {
            destino += "&eliminado=1";
        } else {
            destino += "&error=eliminar";
        }
        response.sendRedirect(destino);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
        if ("eliminar".equals(request.getParameter("accion"))) {
            eliminar(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/ControladorActivo?accion=listar");
    }
}
