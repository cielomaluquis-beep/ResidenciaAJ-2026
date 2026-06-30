/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.ContratoDaoImpl;
import Interface.IContrato;
import Model.Contrato;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "ContratoController", urlPatterns = {"/ContratoController"})
public class ContratoController extends HttpServlet {

    private final IContrato cDao = new ContratoDaoImpl();
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
                guardarContrato(request, response);
                break;
            case "editar":
                editarContrato(request, response);
                break;
            case "eliminar":
                eliminarContrato(request, response);
                break;
            case "buscar":
                buscarContrato(request, response);
                break;
            default:
                listarContratos(request, response);
                break;
        }
        
    }
    private void listarContratos(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Contrato> contratos = cDao.lista();
        response.getWriter().print(gson.toJson(contratos));
    }
    private void guardarContrato(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Contrato c = new Contrato();
        c.setId_persona(Integer.parseInt(request.getParameter("id_persona")));
        c.setId_habitacion(Integer.parseInt(request.getParameter("id_habitacion")));
        c.setFecha_inicio(request.getParameter("fecha_inicio"));
        c.setFecha_fin(request.getParameter("fecha_fin"));
        c.setEstado(1);

        int id = cDao.insert(c);
        response.getWriter().print(gson.toJson(id > 0));
    }

    private void editarContrato(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Contrato c = new Contrato();
        c.setId_contrato(Integer.parseInt(request.getParameter("id_contrato")));
        c.setId_persona(Integer.parseInt(request.getParameter("id_persona")));
        c.setId_habitacion(Integer.parseInt(request.getParameter("id_habitacion")));
        c.setFecha_inicio(request.getParameter("fecha_inicio"));
        c.setFecha_fin(request.getParameter("fecha_fin"));
        c.setEstado(Integer.parseInt(request.getParameter("estado")));

        boolean ok = cDao.update(c);
        response.getWriter().print(gson.toJson(ok));
    }

    private void eliminarContrato(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_contrato"));
        boolean ok = cDao.delete(id);
        response.getWriter().print(gson.toJson(ok));
    }

    private void buscarContrato(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_contrato"));
        Contrato c = cDao.searchById(id);
        response.getWriter().print(gson.toJson(c));
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
        return "ContratoController";
    }

}
