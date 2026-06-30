/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.HabitacionDaoImp;
import Interface.IHabitacion;
import Model.Habitacion;
import com.google.gson.Gson;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author cielo
 */
@WebServlet(name = "HabitacionController", urlPatterns = {"/HabitacionController"})
public class HabitacionController extends HttpServlet {

    private final IHabitacion hDao = new HabitacionDaoImp();
    private final Gson gson = new Gson();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            action = "listar";
        }

        switch (action) {
            case "guardar":
                guardarHabitacion(request, response);
                break;
            case "editar":
                editarHabitacion(request, response);
                break;
            case "eliminar":
                eliminarHabitacion(request, response);
                break;
            case "buscar":
                buscarHabitacion(request, response);
                break;
            default:
                listarHabitaciones(request, response);
                break;
        }
        
    }

   private void listarHabitaciones(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Habitacion> habitaciones = hDao.lista();
        response.getWriter().print(gson.toJson(habitaciones));
    }

    private void guardarHabitacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Habitacion h = new Habitacion();
        h.setNumero(request.getParameter("numero"));
        h.setTipo(request.getParameter("tipo"));
        h.setPrecio(Double.parseDouble(request.getParameter("precio")));
        h.setEstado(1);

        int id = hDao.insert(h);
        response.getWriter().print(gson.toJson(id > 0));
    }

    private void editarHabitacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Habitacion h = new Habitacion();
        h.setId_habitacion(Integer.parseInt(request.getParameter("id_habitacion")));
        h.setNumero(request.getParameter("numero"));
        h.setTipo(request.getParameter("tipo"));
        h.setPrecio(Double.parseDouble(request.getParameter("precio")));
        h.setEstado(Integer.parseInt(request.getParameter("estado")));

        boolean ok = hDao.update(h);
        response.getWriter().print(gson.toJson(ok));
    }

    private void eliminarHabitacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_habitacion"));
        boolean ok = hDao.delete(id);
        response.getWriter().print(gson.toJson(ok));
    }

    private void buscarHabitacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_habitacion"));
        Habitacion h = hDao.searchById(id);
        response.getWriter().print(gson.toJson(h));
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

    
    @Override
    public String getServletInfo() {
        return "HabitacionController";
    }

}
