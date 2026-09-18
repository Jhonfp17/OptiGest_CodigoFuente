
package Servlet;

import Controlador.ActivosDAO;
import Controlador.CategoriasDAO;
import Controlador.Estado_ActivoDAO;
import Controlador.HistorialCambiosDAO;
import Controlador.ProveedoresDAO;
import Modelo.Activos;
import Modelo.Personal;
import Seguridad.AuditoriaContexto;
import Servicio.ValidacionActivo;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/EditarActivo")
public class EditarActivo extends HttpServlet {

    private void cargarListas(HttpServletRequest request) {
        request.setAttribute("estadosActivo", new Estado_ActivoDAO().listar());
        request.setAttribute("categorias",    new CategoriasDAO().listar());
        request.setAttribute("proveedores",   new ProveedoresDAO().listar());
    }
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
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ActivosDAO dao = new ActivosDAO();
            Activos act = dao.consultar(id);

            if (act == null || !dao.estaActivo(id)) {
                response.sendRedirect(request.getContextPath()
                        + "/ControladorActivo?accion=listar&error=activo_retirado");
                return;
            }
            request.setAttribute("activo", act);
            cargarListas(request);
            request.setAttribute("historial", new HistorialCambiosDAO().listarPorRegistro("Activos", id));

            request.getRequestDispatcher("/Vista/EditarActivo.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath()
                    + "/ControladorActivo?accion=listar");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
        String motivo = request.getParameter("motivo");
        if (motivo == null || motivo.trim().isEmpty()) {
            request.setAttribute("mensaje", "Indica el motivo del cambio para conservar la trazabilidad.");
            try {
                int id = Integer.parseInt(request.getParameter("id_activos"));
                Activos actual = new ActivosDAO().consultar(id);
                request.setAttribute("activo", actual);
                request.setAttribute("historial", new HistorialCambiosDAO().listarPorRegistro("Activos", id));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/ControladorActivo?accion=listar");
                return;
            }
            cargarListas(request);
            request.getRequestDispatcher("/Vista/EditarActivo.jsp").forward(request, response);
            return;
        }

        try {
            Activos act = new Activos();
            act.setId_activos(Integer.parseInt(request.getParameter("id_activos")));
            ActivosDAO dao = new ActivosDAO();
            if (!dao.estaActivo(act.getId_activos())) {
                response.sendRedirect(request.getContextPath()
                        + "/ControladorActivo?accion=listar&error=activo_retirado");
                return;
            }
            act.setNombre_activos(request.getParameter("nombre_activos"));
            act.setCodigo_act(request.getParameter("codigo_act"));
            act.setValor(request.getParameter("valor"));
            act.setVida_util(Math.max(0, Integer.parseInt(request.getParameter("vida_util"))));
            act.setFecha_adquma(request.getParameter("fecha_adquma"));
            // La devolución se registra exclusivamente en la asignación.
            act.setFecha_devolucion(null);
            act.setDescripcion(request.getParameter("descripcion"));
            act.setEstado_Activo_idEstado_Activo(
                Integer.parseInt(request.getParameter("Estado_Activo_idEstado_Activo")));
            act.setCategorias_idCategorias(
                Integer.parseInt(request.getParameter("Categorias_idCategorias")));
            act.setProveedores_idProveedores(
                Integer.parseInt(request.getParameter("Proveedores_idProveedores")));

            String errorValidacion = ValidacionActivo.normalizarYValidar(act);
            if (errorValidacion != null) {
                request.setAttribute("mensaje", errorValidacion);
                request.setAttribute("activo", act);
                cargarListas(request);
                request.setAttribute("historial", new HistorialCambiosDAO().listarPorRegistro("Activos", act.getId_activos()));
                request.getRequestDispatcher("/Vista/EditarActivo.jsp").forward(request, response);
                return;
            }

            if (!dao.codigoDisponible(act.getCodigo_act(), act.getId_activos())) {
                request.setAttribute("mensaje", "No se pudo guardar: ya existe otro activo con ese codigo.");
                request.setAttribute("activo", act);
                cargarListas(request);
                request.setAttribute("historial", new HistorialCambiosDAO().listarPorRegistro("Activos", act.getId_activos()));
                request.getRequestDispatcher("/Vista/EditarActivo.jsp").forward(request, response);
                return;
            }

            AuditoriaContexto.establecer(admin, motivo);
            boolean actualizado;
            try {
                actualizado = dao.actualizar(act);
            } finally {
                AuditoriaContexto.limpiar();
            }
            if (actualizado) {
                response.sendRedirect(request.getContextPath()
                        + "/ControladorActivo?accion=listar&actualizado=1");
            } else {
                request.setAttribute("mensaje",
                        "Error al actualizar. Verifica los datos e intenta de nuevo.");
                request.setAttribute("activo", act);
                cargarListas(request);
                request.setAttribute("historial", new HistorialCambiosDAO().listar("Activos", String.valueOf(act.getId_activos())));
                request.getRequestDispatcher("/Vista/EditarActivo.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("mensaje",
                    "Revisa vida util, estado, categoria y proveedor.");
            cargarListas(request);
            request.getRequestDispatcher("/Vista/EditarActivo.jsp")
                    .forward(request, response);
        }
    }
}
