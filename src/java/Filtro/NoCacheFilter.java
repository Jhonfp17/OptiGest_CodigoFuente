package Filtro;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Evita que el navegador reutilice páginas autenticadas desde su caché. */
@WebFilter("/*")
public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String ruta = httpRequest.getRequestURI()
                .substring(httpRequest.getContextPath().length()).toLowerCase();

        if (esVistaProtegida(ruta)) {
            HttpSession sesion = httpRequest.getSession(false);
            if (sesion == null || sesion.getAttribute("usuarioLogueado") == null) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/Vista/InicioSesion.jsp");
                return;
            }
        }

        if (!esRecursoEstatico(ruta)) {
            httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
        }
        chain.doFilter(request, response);
    }

    private boolean esRecursoEstatico(String ruta) {
        return ruta.endsWith(".css") || ruta.endsWith(".js") || ruta.endsWith(".jpg")
                || ruta.endsWith(".jpeg") || ruta.endsWith(".png") || ruta.endsWith(".gif")
                || ruta.endsWith(".svg") || ruta.endsWith(".ico") || ruta.endsWith(".woff")
                || ruta.endsWith(".woff2");
    }

    private boolean esVistaProtegida(String ruta) {
        if (!ruta.startsWith("/vista/")) return false;
        return !ruta.endsWith("/iniciosesion.jsp")
                && !ruta.startsWith("/vista/recuperar")
                && !ruta.endsWith("/contacto.jsp");
    }
}
