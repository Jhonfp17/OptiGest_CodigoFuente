package Servlet;

import Controlador.ContactoDAO;
import Controlador.DocumentoDAO;
import Modelo.Documento;
import Modelo.MensajeContacto;
import java.util.Locale;
import java.text.Normalizer;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Contacto")
public class Contacto extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("documentos", new DocumentoDAO().listar());
        req.getRequestDispatcher("/Vista/Contacto.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        MensajeContacto solicitud = new MensajeContacto();
        solicitud.setNombre(valor(req.getParameter("nombre"))); solicitud.setApellidos(valor(req.getParameter("apellidos")));
        String tipoDocumento = descripcionDocumento(req.getParameter("tipoDocumentoId"));
        solicitud.setTipoDocumento(mayusculas(tipoDocumento));
        solicitud.setDocumento(valor(req.getParameter("documento"))); solicitud.setEmail(valor(req.getParameter("email")));
        solicitud.setTelefono(valor(req.getParameter("telefono"))); solicitud.setDireccion(valor(req.getParameter("direccion"))); solicitud.setTipoSolicitud(valor(req.getParameter("tipoSolicitud")));
        solicitud.setUsuarioDeseado(valor(req.getParameter("usuarioDeseado"))); solicitud.setAsunto(valor(req.getParameter("asunto")));
        solicitud.setMensaje(valor(req.getParameter("mensaje")));
        solicitud.setNombre(mayusculas(solicitud.getNombre())); solicitud.setApellidos(mayusculas(solicitud.getApellidos()));
        solicitud.setDireccion(mayusculas(solicitud.getDireccion())); solicitud.setTipoSolicitud(mayusculas(solicitud.getTipoSolicitud()));
        solicitud.setUsuarioDeseado(mayusculas(solicitud.getUsuarioDeseado()));
        solicitud.setAsunto(mayusculas(solicitud.getAsunto())); solicitud.setMensaje(mayusculas(solicitud.getMensaje()));
        solicitud.setDocumento(solicitud.getDocumento().replaceAll("\\D", "")); solicitud.setTelefono(solicitud.getTelefono().replaceAll("\\D", ""));
        boolean completo = !solicitud.getNombre().isEmpty() && !solicitud.getApellidos().isEmpty() && !solicitud.getDocumento().isEmpty()
                && !tipoDocumento.isEmpty() && !solicitud.getEmail().isEmpty() && !solicitud.getTelefono().isEmpty() && !solicitud.getDireccion().isEmpty() && !solicitud.getTipoSolicitud().isEmpty()
                && !solicitud.getAsunto().isEmpty() && !solicitud.getMensaje().isEmpty() && req.getParameter("aceptaDatos") != null;
        boolean solicitudValida = "CREAR CUENTA".equals(solicitud.getTipoSolicitud())
                || "RECUPERAR ACCESO".equals(solicitud.getTipoSolicitud())
                || "ACTUALIZAR DATOS".equals(solicitud.getTipoSolicitud())
                || "OTRO".equals(solicitud.getTipoSolicitud());
        boolean usuarioDeseadoValido = solicitud.getUsuarioDeseado().isEmpty()
                || solicitud.getUsuarioDeseado().matches("[\\p{L}0-9._-]{3,45}");
        boolean valido = solicitud.getNombre().matches("[\\p{L} ]{2,45}") && solicitud.getApellidos().matches("[\\p{L} ]{2,45}")
                && documentoValido(solicitud.getDocumento(), tipoDocumento) && solicitud.getTelefono().matches("\\d{7,15}")
                && solicitud.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$") && solicitud.getEmail().length() >= 6 && solicitud.getEmail().length() <= 45
                && solicitud.getDireccion().matches("[\\p{L}0-9#.,/\\- ]{5,45}") && solicitudValida && usuarioDeseadoValido
                && solicitud.getAsunto().length() >= 3 && solicitud.getAsunto().length() <= 100 && solicitud.getMensaje().length() <= 200;
        if (!completo || !valido || ("CREAR CUENTA".equals(solicitud.getTipoSolicitud()) && solicitud.getUsuarioDeseado().isEmpty())) { resp.sendRedirect(req.getContextPath() + "/Contacto?error=campos"); return; }
        resp.sendRedirect(req.getContextPath() + "/Contacto?" + (new ContactoDAO().guardar(solicitud) ? "enviado=1" : "error=guardar"));
    }
    private String valor(String valor) { return valor == null ? "" : valor.trim(); }
    private String descripcionDocumento(String idTexto) {
        try {
            Documento documento = new DocumentoDAO().consultar(Integer.parseInt(valor(idTexto)));
            return documento == null ? "" : valor(documento.getDescripcion_doc());
        } catch (NumberFormatException e) {
            return "";
        }
    }
    private boolean documentoValido(String numero, String tipoDocumento) {
        String tipo = Normalizer.normalize(tipoDocumento, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.forLanguageTag("es-CO"));
        if (tipo.contains("tarjeta") || tipo.matches(".*\\bti\\b.*")) return numero.matches("\\d{10,11}");
        if (tipo.contains("extranjer") || tipo.matches(".*\\bce\\b.*")) return numero.matches("\\d{6,10}");
        if (tipo.contains("nit")) return numero.matches("\\d{9,10}");
        if (tipo.contains("cedula") || tipo.matches(".*\\bcc\\b.*")) return numero.matches("\\d{6,10}");
        return numero.matches("\\d{5,20}");
    }
    private String mayusculas(String valor) { return Servicio.NormalizadorTexto.mayusculas(valor); }
}
