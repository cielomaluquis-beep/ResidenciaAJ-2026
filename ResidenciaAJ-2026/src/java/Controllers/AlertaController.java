package Controllers;

import Dao.AlertaDaoImpl;
import Interface.IAlerta;
import Model.Alerta;
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

@WebServlet(name = "AlertaController", urlPatterns = {"/AlertaController"})
public class AlertaController extends HttpServlet {

    private IAlerta aDao;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        aDao = new AlertaDaoImpl();
        gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        HttpSession session = request.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null) {
            out.print("{\"status\":\"error\", \"message\":\"No autenticado\"}");
            return;
        }

        switch (action) {
            case "listar":
                List<Alerta> lista = aDao.listarPorUsuario(u.getId_usuario());
                out.print(gson.toJson(lista));
                break;
            case "contar":
                int count = aDao.contarNoLeidas(u.getId_usuario());
                out.print("{\"count\":" + count + "}");
                break;
            case "marcarLeida":
                int id = Integer.parseInt(request.getParameter("id_alerta"));
                if (aDao.marcarLeida(id)) {
                    out.print("{\"status\":\"success\"}");
                } else {
                    out.print("{\"status\":\"error\"}");
                }
                break;
            case "marcarTodasLeidas":
                if (aDao.marcarTodasLeidas(u.getId_usuario())) {
                    out.print("{\"status\":\"success\"}");
                } else {
                    out.print("{\"status\":\"error\"}");
                }
                break;
        }
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
