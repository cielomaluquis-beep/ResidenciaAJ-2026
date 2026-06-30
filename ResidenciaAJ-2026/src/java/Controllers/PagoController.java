/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.PagoDaoImpl;
import Interface.IPago;
import Model.Pago;
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
@WebServlet(name = "PagoController", urlPatterns = {"/PagoController"})
public class PagoController extends HttpServlet {

    private final IPago pDao = new PagoDaoImpl();
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
                guardarPago(request, response);
                break;
            case "editar":
                editarPago(request, response);
                break;
            case "eliminar":
                eliminarPago(request, response);
                break;
            case "buscar":
                buscarPago(request, response);
                break;
            default:
                listarPagos(request, response);
                break;
        }
        
    }

    private void listarPagos(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Pago> pagos = pDao.lista();
        response.getWriter().print(gson.toJson(pagos));
    }

    private void guardarPago(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Pago p = new Pago();
        p.setId_contrato(Integer.parseInt(request.getParameter("id_contrato")));
        p.setMonto(Double.parseDouble(request.getParameter("monto")));
        p.setFecha(request.getParameter("fecha"));
        p.setMetodo_pago(request.getParameter("metodo_pago"));

        int id = pDao.insert(p);
        response.getWriter().print(gson.toJson(id > 0));
    }

    private void editarPago(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Pago p = new Pago();
        p.setId_pago(Integer.parseInt(request.getParameter("id_pago")));
        p.setId_contrato(Integer.parseInt(request.getParameter("id_contrato")));
        p.setMonto(Double.parseDouble(request.getParameter("monto")));
        p.setFecha(request.getParameter("fecha"));
        p.setMetodo_pago(request.getParameter("metodo_pago"));

        boolean ok = pDao.update(p);
        response.getWriter().print(gson.toJson(ok));
    }

    private void eliminarPago(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_pago"));
        boolean ok = pDao.delete(id);
        response.getWriter().print(gson.toJson(ok));
    }

    private void buscarPago(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_pago"));
        Pago p = pDao.searchById(id);
        response.getWriter().print(gson.toJson(p));
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
        return "PagoController";
    }

}
