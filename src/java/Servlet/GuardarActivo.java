package Servlet;

import Controlador.ActivosDAO;
import Controlador.CategoriasDAO;
import Controlador.Estado_ActivoDAO;
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

@WebServlet("/GuardarActivo")
public class GuardarActivo extends HttpServlet {

    private void cargarListas(HttpServletRequest request) {
        request.setAttribute("estadosActivo", new Estado_ActivoDAO().listar());
        request.setAttribute("categorias", new CategoriasDAO().listar());
        request.setAttribute("proveedores", new ProveedoresDAO().listar());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("usuarioLogueado") == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            Personal admin = (Personal) session.getAttribute("usuarioLogueado");

            if (!admin.esAdministrador()) {
                response.sendRedirect(request.getContextPath() + "/VerPerfil");
                return;
            }

            cargarListas(request);

            request.getRequestDispatcher("/Vista/RegistroDeActivos.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.out.println("Error en GuardarActivo doGet: " + e.getMessage());

            request.setAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            request.getRequestDispatcher("/Vista/GestionActivos.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("usuarioLogueado") == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            Personal admin = (Personal) session.getAttribute("usuarioLogueado");

            if (!admin.esAdministrador()) {
                response.sendRedirect(request.getContextPath() + "/VerPerfil");
                return;
            }

            Activos act = new Activos();

            act.setCodigo_act(request.getParameter("codigo_act"));
            act.setNombre_activos(request.getParameter("nombre_activos"));
            act.setValor(request.getParameter("valor"));
            act.setFecha_adquma(request.getParameter("fecha_adquma"));
            // La devolución se registra exclusivamente en la asignación.
            act.setFecha_devolucion(null);
            act.setVida_util(Math.max(0, Integer.parseInt(request.getParameter("vida_util"))));
            act.setDescripcion(request.getParameter("descripcion"));
            act.setEstado_Activo_idEstado_Activo(Integer.parseInt(request.getParameter("Estado_Activo_idEstado_Activo")));
            act.setCategorias_idCategorias(Integer.parseInt(request.getParameter("Categorias_idCategorias")));
            act.setProveedores_idProveedores(Integer.parseInt(request.getParameter("Proveedores_idProveedores")));

            String errorValidacion = ValidacionActivo.normalizarYValidar(act);
            if (errorValidacion != null) {
                request.setAttribute("mensaje", errorValidacion);
                cargarListas(request);
                request.getRequestDispatcher("/Vista/RegistroDeActivos.jsp").forward(request, response);
                return;
            }

            ActivosDAO dao = new ActivosDAO();

            if (!dao.codigoDisponible(act.getCodigo_act(), 0)) {
                request.setAttribute("mensaje", "No se pudo registrar: ya existe un activo con ese codigo.");
                request.getRequestDispatcher("/Vista/RegistroDeActivos.jsp").forward(request, response);
                return;
            }

            AuditoriaContexto.establecer(admin, "Registro de activo");
            boolean guardado;
            try {
                guardado = dao.insertar(act);
            } finally {
                AuditoriaContexto.limpiar();
            }
            if (guardado) {
                String destino = "configuracion".equals(request.getParameter("origen"))
                        ? "/GestionCatalogo?tipo=activos&guardado=1"
                        : "/ControladorActivo?accion=listar&guardado=1";
                response.sendRedirect(request.getContextPath() + destino);
            } else {
                request.setAttribute("mensaje", "Error al registrar. Verifica los datos e intenta de nuevo.");
                cargarListas(request);

                request.getRequestDispatcher("/Vista/RegistroDeActivos.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            System.out.println("Error de formato numérico en GuardarActivo: " + e.getMessage());

            request.setAttribute("mensaje", "Revisa vida útil, estado, categoría y proveedor.");
            cargarListas(request);

            request.getRequestDispatcher("/Vista/RegistroDeActivos.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            System.out.println("Error en GuardarActivo doPost: " + e.getMessage());

            request.setAttribute("mensaje", "Ocurrió un error al registrar el activo.");
            cargarListas(request);

            request.getRequestDispatcher("/Vista/RegistroDeActivos.jsp")
                    .forward(request, response);
        }
    }
}
