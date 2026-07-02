package Util;

import Model.Permiso;
import java.io.IOException;
import java.util.Map;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro de seguridad para proteger las páginas privadas mediante RBAC.
 * NOTA: La verificación de permisos por rol está deshabilitada temporalmente.
 * Reactivar cuando la tabla 'permisos_rol_modulo' y 'modulos' tengan datos.
 */
@WebFilter(urlPatterns = {"/dashboard.html", "/habitaciones.html", "/contratos.html", "/pagos.html", "/seguridad.html"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean loggedIn = session != null && session.getAttribute("usuario") != null;

        if (loggedIn) {
            String path = req.getRequestURI().substring(req.getContextPath().length());
            Map<String, Permiso> permisos = (Map<String, Permiso>) session.getAttribute("permisos");

            // Bypass: si el rol es ADMIN, tiene acceso a todo sin verificar tabla
            Model.Usuario usuarioSesion = (Model.Usuario) session.getAttribute("usuario");
            boolean isAdmin = usuarioSesion != null
                    && usuarioSesion.getId_rol() == 1; // 1 = Admin

            boolean hasPermission = isAdmin; // Admin pasa siempre

            if (!hasPermission && permisos != null && permisos.containsKey(path)) {
                Permiso p = permisos.get(path);
                if (p.isVerModulo()) {
                    hasPermission = true;
                }
            } else if (!hasPermission && path.equals("/dashboard.html")) {
                // Dejar pasar al dashboard temporalmente para que de ahi se redirija
                hasPermission = true;
            } else if (!hasPermission && usuarioSesion != null && usuarioSesion.getId_rol() == 3) {
                // Bypass para vistas exclusivas de cliente
                if (path.equals("/catalogo.html") || path.equals("/mis_reservas.html")) {
                    hasPermission = true;
                }
            }

            if (hasPermission) {
                chain.doFilter(request, response);
            } else {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso Denegado: Tu rol no tiene permisos para ver este módulo.");
            }
        } else {
            res.sendRedirect(req.getContextPath() + "/login.html");
        }
    }

    @Override
    public void destroy() {}
}

