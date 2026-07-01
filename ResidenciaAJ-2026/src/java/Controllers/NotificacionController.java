package Controllers;

import Interface.INotificacion;
import Dao.NotificacionDaoImpl;
import Model.Notificacion;
import Model.Usuario;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "NotificacionController", urlPatterns = {"/NotificacionController"})
public class NotificacionController extends HttpServlet {

    private INotificacion notificacionDao;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        notificacionDao = new NotificacionDaoImpl();
        gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        switch (action) {
            case "listar":
                listar(request, response);
                break;
            case "marcarLeida":
                marcarLeida(request, response);
                break;
            case "marcarTodas":
                marcarTodasLeidas(request, response);
                break;
            case "eliminar":
                eliminar(request, response);
                break;
            case "pendientes":
                pendientes(request, response);
                break;
            default:
                listar(request, response);
                break;
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario u = (Usuario) session.getAttribute("usuario");
            List<Notificacion> lista = notificacionDao.listarPorUsuario(u.getId_usuario());
            out.print(gson.toJson(lista));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("[]");
        }
        out.flush();
    }
    
    private void pendientes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario u = (Usuario) session.getAttribute("usuario");
            List<Notificacion> lista = notificacionDao.listarPorUsuario(u.getId_usuario());
            long count = lista.stream().filter(n -> n.getLeido() == 0).count();
            out.print("{\"pendientes\": " + count + "}");
        } else {
            out.print("{\"pendientes\": 0}");
        }
        out.flush();
    }

    private void marcarLeida(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario u = (Usuario) session.getAttribute("usuario");
            int id_notificacion = Integer.parseInt(request.getParameter("id_notificacion"));
            boolean ok = notificacionDao.marcarLeida(id_notificacion, u.getId_usuario());
            out.print(ok);
        } else {
            out.print(false);
        }
        out.flush();
    }

    private void marcarTodasLeidas(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario u = (Usuario) session.getAttribute("usuario");
            boolean ok = notificacionDao.marcarTodasLeidas(u.getId_usuario());
            out.print(ok);
        } else {
            out.print(false);
        }
        out.flush();
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario u = (Usuario) session.getAttribute("usuario");
            int id_notificacion = Integer.parseInt(request.getParameter("id_notificacion"));
            boolean ok = notificacionDao.eliminar(id_notificacion, u.getId_usuario());
            out.print(ok);
        } else {
            out.print(false);
        }
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
