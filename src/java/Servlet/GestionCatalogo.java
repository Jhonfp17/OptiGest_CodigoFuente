package Servlet;

import Controlador.CategoriasDAO;
import Controlador.DocumentoDAO;
import Controlador.RolesDAO;
import Controlador.HorariosDAO;
import Controlador.DiasDAO;
import Controlador.MantenimientoDAO;
import Controlador.Estado_ActivoDAO;
import Controlador.Estado_PersonalDAO;
import Controlador.ActivosDAO;
import Controlador.AsignacionesDAO;
import Controlador.PersonalDAO;
import Controlador.Programacion_PersonalDAO;
import Controlador.ProveedoresDAO;
import Modelo.Activos;
import Modelo.Categorias;
import Modelo.Dias;
import Modelo.Documento;
import Modelo.Estado_Activo;
import Modelo.Estado_Personal;
import Modelo.Horarios;
import Modelo.Personal;
import Modelo.Proveedores;
import Modelo.Roles;

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

@WebServlet("/GestionCatalogo")
public class GestionCatalogo extends HttpServlet {

    private final RolesDAO rolesDAO = new RolesDAO();
    private final DocumentoDAO documentoDAO = new DocumentoDAO();
    private final CategoriasDAO categoriasDAO = new CategoriasDAO();
    private final HorariosDAO horariosDAO = new HorariosDAO();
    private final DiasDAO diasDAO = new DiasDAO();
    private final MantenimientoDAO mantenimientoDAO = new MantenimientoDAO();
    private final Estado_ActivoDAO estadoActivoDAO = new Estado_ActivoDAO();
    private final Estado_PersonalDAO estadoPersonalDAO = new Estado_PersonalDAO();
    private final ProveedoresDAO proveedoresDAO = new ProveedoresDAO();
    private final PersonalDAO personalDAO = new PersonalDAO();
    private final ActivosDAO activosDAO = new ActivosDAO();
    private final AsignacionesDAO asignacionesDAO = new AsignacionesDAO();
    private final Programacion_PersonalDAO programacionPersonalDAO = new Programacion_PersonalDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
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

        String tipo = request.getParameter("tipo");

        if (tipo == null) {
            tipo = "";
        }

        if ("asignaciones".equals(tipo)) {
            session.setAttribute("origenAsignaciones", "configuracion");
        }

        request.setAttribute("tipo", tipo);
        cargarMapasRelacionados(request);

        switch (tipo) {

            case "roles":
                request.setAttribute("titulo", "Roles");
                request.setAttribute("datos", rolesDAO.listar());
                break;

            case "documentos":
                request.setAttribute("titulo", "Documentos");
                request.setAttribute("datos", documentoDAO.listar());
                break;

            case "categorias":
                request.setAttribute("titulo", "Categorías");
                request.setAttribute("datos", categoriasDAO.listar());
                break;

            case "estadoPersonal":
                request.setAttribute("titulo", "Estado Personal");
                request.setAttribute("datos", estadoPersonalDAO.listar());
                break;

            case "estadoActivo":
                request.setAttribute("titulo", "Estado Activo");
                request.setAttribute("datos", estadoActivoDAO.listar());
                break;

            case "proveedores":
                request.setAttribute("titulo", "Proveedores");
                request.setAttribute("datos", proveedoresDAO.listar());
                break;

            case "horarios":
                request.setAttribute("titulo", "Horarios");
                request.setAttribute("datos", horariosDAO.consultar());
                break;

            case "dias":
                request.setAttribute("titulo", "Días");
                request.setAttribute("datos", diasDAO.listar());
                break;

            case "mantenimiento":
                request.setAttribute("titulo", "Mantenimiento");
                request.setAttribute("datos", mantenimientoDAO.consultar());
                break;

            case "personal":
                request.setAttribute("titulo", "Personal");
                request.setAttribute("datos", personalDAO.consultarTodo());
                break;

            case "activos":
                request.setAttribute("titulo", "Activos");
                request.setAttribute("datos", activosDAO.listar());
                break;

            case "asignaciones":
                request.setAttribute("titulo", "Asignaciones");
                request.setAttribute("datos", asignacionesDAO.listar());
                break;

            case "programacionPersonal":
                request.setAttribute("titulo", "Programación Personal");
                request.setAttribute("datos", programacionPersonalDAO.listar());
                break;

            default:
                request.setAttribute("titulo", "Catálogo");
                break;
        }

        request.getRequestDispatcher(
                "/Vista/GestionCatalogo.jsp")
                .forward(request, response);
    }

    private void cargarMapasRelacionados(HttpServletRequest request) {
        request.setAttribute("documentosMap", mapaDocumentos(documentoDAO.listar()));
        request.setAttribute("rolesMap", mapaRoles(rolesDAO.listar()));
        request.setAttribute("estadoPersonalMap", mapaEstadoPersonal(estadoPersonalDAO.listar()));
        request.setAttribute("estadoActivoMap", mapaEstadoActivo(estadoActivoDAO.listar()));
        request.setAttribute("categoriasMap", mapaCategorias(categoriasDAO.listar()));
        request.setAttribute("proveedoresMap", mapaProveedores(proveedoresDAO.listar()));
        request.setAttribute("activosMap", mapaActivos(activosDAO.listar()));
        request.setAttribute("personalMap", mapaPersonal(personalDAO.consultarTodo()));
        request.setAttribute("diasMap", mapaDias(diasDAO.listar()));
        request.setAttribute("horariosMap", mapaHorarios(horariosDAO.consultar()));
    }

    private Map<Integer, String> mapaDocumentos(List<Documento> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Documento item : lista) {
            mapa.put(item.getId_documento(), item.getDescripcion_doc());
        }
        return mapa;
    }

    private Map<Integer, String> mapaRoles(List<Roles> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Roles item : lista) {
            mapa.put(item.getIdRoles(), item.getDescripcion_roles());
        }
        return mapa;
    }

    private Map<Integer, String> mapaEstadoPersonal(List<Estado_Personal> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Estado_Personal item : lista) {
            mapa.put(item.getId_estado(), item.getDescripcion_estado());
        }
        return mapa;
    }

    private Map<Integer, String> mapaEstadoActivo(List<Estado_Activo> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Estado_Activo item : lista) {
            mapa.put(item.getIdEstado_Activo(), item.getDescripcion_activo());
        }
        return mapa;
    }

    private Map<Integer, String> mapaCategorias(List<Categorias> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Categorias item : lista) {
            mapa.put(item.getIdCategorias(), item.getDescripcionCategoria());
        }
        return mapa;
    }

    private Map<Integer, String> mapaProveedores(List<Proveedores> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Proveedores item : lista) {
            mapa.put(item.getIdProveedores(), item.getNombre());
        }
        return mapa;
    }

    private Map<Integer, String> mapaActivos(List<Activos> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Activos item : lista) {
            mapa.put(item.getId_activos(), item.getNombre_activos());
        }
        return mapa;
    }

    private Map<Integer, String> mapaPersonal(List<Personal> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Personal item : lista) {
            mapa.put(item.getIdPersonal(), item.getNombre() + " " + item.getApellidos());
        }
        return mapa;
    }

    private Map<Integer, String> mapaDias(List<Dias> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Dias item : lista) {
            mapa.put(item.getIdDias(), item.getDescripcionDias());
        }
        return mapa;
    }

    private Map<Integer, String> mapaHorarios(List<Horarios> lista) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Horarios item : lista) {
            mapa.put(item.getId_horarios(), item.getDescripcion());
        }
        return mapa;
    }
}
