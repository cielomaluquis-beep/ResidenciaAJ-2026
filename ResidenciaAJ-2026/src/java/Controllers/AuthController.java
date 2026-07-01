package Controllers;

import Dao.UsuarioDaoImpl;
import Dao.PermisoDaoImpl;
import Interface.IUsuario;
import Interface.IPermiso;
import Model.Usuario;
import Model.Permiso;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AuthController", urlPatterns = {"/AuthController"})
public class AuthController extends HttpServlet {
    private final IUsuario uDao = new UsuarioDaoImpl();
    private final IPermiso pDao = new PermisoDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        java.io.PrintWriter out = response.getWriter();

        if ("validar".equals(action)) {
            String email = request.getParameter("usuario"); 
            String pasw = request.getParameter("password");
            
            Usuario us = uDao.validate(email, pasw);

            if (us != null && us.getEmail() != null) {
                HttpSession sesion = request.getSession(true);
                sesion.setAttribute("usuario", us);
                
                // Cargar permisos
                List<Permiso> permisos = pDao.getPermisosByRol(us.getId_rol());
                Map<String, Permiso> mapaPermisos = new HashMap<>();
                for (Permiso p : permisos) {
                    if (p.getModulo() != null && p.getModulo().getRuta() != null) {
                        mapaPermisos.put(p.getModulo().getRuta(), p);
                    }
                }
                sesion.setAttribute("permisos", mapaPermisos);

                // Llama al evaluador de alertas al iniciar sesion
                try {
                    Util.AlertaService.evaluarAlertas();
                } catch (Exception e) {
                    System.out.println("Error evaluando alertas: " + e.getMessage());
                }

                String redirectUrl = "dashboard.html";
                if (us.getId_rol() == 3) { // 3 es Cliente
                    redirectUrl = "catalogo.html";
                }

                out.print("{\"status\":\"success\", \"redirect\":\"" + redirectUrl + "\"}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"Usuario o contraseña incorrectos\"}");
            }

        } else if ("registrarCliente".equals(action)) {
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String dni = request.getParameter("dni");
            String telefono = request.getParameter("telefono");
            String correo = request.getParameter("correo");
            String password = request.getParameter("password");

            // Crear Usuario
            Usuario nuevoU = new Usuario();
            nuevoU.setNombre(nombre + " " + apellidos);
            nuevoU.setEmail(correo);
            nuevoU.setContrasena(nuevoU.HashPassword(password));
            nuevoU.setHabilitado(1);
            nuevoU.setId_rol(3); // 3 = Cliente

            int newUserId = uDao.insert(nuevoU);

            if (newUserId > 0) {
                // Crear Cliente
                Dao.ClienteDaoImp cDao = new Dao.ClienteDaoImp();
                Model.Cliente nuevoC = new Model.Cliente();
                nuevoC.setNombre(nombre);
                
                String[] partesApellido = apellidos.trim().split(" ", 2);
                nuevoC.setAp_paterno(partesApellido[0]);
                if (partesApellido.length > 1) {
                    nuevoC.setAp_materno(partesApellido[1]);
                } else {
                    nuevoC.setAp_materno("");
                }
                
                nuevoC.setNro_documento(dni);
                nuevoC.setTelefono(telefono);
                nuevoC.setEmail(correo);
                
                cDao.insert(nuevoC);

                // Auto-login after successful registration
                Usuario us = uDao.validate(correo, password);
                if (us != null && us.getEmail() != null) {
                    HttpSession sesion = request.getSession(true);
                    sesion.setAttribute("usuario", us);
                    
                    List<Permiso> permisos = pDao.getPermisosByRol(us.getId_rol());
                    Map<String, Permiso> mapaPermisos = new HashMap<>();
                    for (Permiso p : permisos) {
                        if (p.getModulo() != null && p.getModulo().getRuta() != null) {
                            mapaPermisos.put(p.getModulo().getRuta(), p);
                        }
                    }
                    sesion.setAttribute("permisos", mapaPermisos);
                }

                out.print("{\"status\":\"success\", \"redirect\":\"catalogo.html\"}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"El correo ya existe o ocurrió un error.\"}");
            }

        } else if ("Salir".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("login.html");
        } else {
            response.sendRedirect("login.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("me".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("usuario") != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                Usuario u = (Usuario) session.getAttribute("usuario");
                // Remove password hash before sending to frontend
                u.setContrasena(null);
                
                java.util.Map<String, Object> responseData = new java.util.HashMap<>();
                responseData.put("usuario", u);
                responseData.put("permisos", session.getAttribute("permisos"));
                
                String json = new com.google.gson.Gson().toJson(responseData);
                response.getWriter().write(json);
            } else {
                response.setStatus(401);
            }
        } else if ("Salir".equals(action)) {
            doPost(request, response);
        } else {
            response.sendRedirect("login.html");
        }
    }

    @Override
    public String getServletInfo() {
        return "AuthController Servlet";
    }
}
