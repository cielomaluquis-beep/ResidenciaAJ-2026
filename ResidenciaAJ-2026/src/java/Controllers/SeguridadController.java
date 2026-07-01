package Controllers;

import Dao.PermisoDaoImpl;
import Dao.RolDaoImpl;
import Interface.IPermiso;
import Interface.IRol;
import Model.Permiso;
import Model.Rol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SeguridadController", urlPatterns = {"/SeguridadController"})
public class SeguridadController extends HttpServlet {
    
    private final IRol rDao = new RolDaoImpl();
    private final IPermiso pDao = new PermisoDaoImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            if ("listarRoles".equals(action)) {
                List<Rol> roles = rDao.lista();
                out.print(gson.toJson(roles));
            } else if ("listarPermisos".equals(action)) {
                String idRolStr = request.getParameter("id_rol");
                if (idRolStr != null) {
                    int idRol = Integer.parseInt(idRolStr);
                    List<Permiso> permisos = pDao.getAllModulosConPermisos(idRol);
                    out.print(gson.toJson(permisos));
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            if ("guardarPermisos".equals(action)) {
                int idRol = Integer.parseInt(request.getParameter("id_rol"));
                String permisosJson = request.getParameter("permisos");
                
                Type listType = new TypeToken<List<Permiso>>(){}.getType();
                List<Permiso> permisos = gson.fromJson(permisosJson, listType);
                
                boolean ok = pDao.savePermisos(idRol, permisos);
                if (ok) {
                    out.print("{\"status\":\"success\"}");
                } else {
                    out.print("{\"status\":\"error\"}");
                }
            }
        }
    }
}
