/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Dao.PersonaDaoImpl;
import Interface.IPersona;
import Model.Persona;
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
@WebServlet(name = "PersonaController", urlPatterns = {"/PersonaController"})
public class PersonaController extends HttpServlet {

    private final IPersona pDao = new PersonaDaoImpl();
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
                guardarPersona(request, response);
                break;
            case "editar":
                editarPersona(request, response);
                break;
            case "eliminar":
                eliminarPersona(request, response);
                break;
            case "buscar":
                buscarPersona(request, response);
                break;
            default:
                listarPersonas(request, response);
                break;
        }
    }

    private void listarPersonas(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Persona> personas = pDao.lista();
        response.getWriter().print(gson.toJson(personas));
    }

    private void guardarPersona(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Persona p = new Persona();
        p.setNombre(request.getParameter("nombre"));
        p.setApellidos(request.getParameter("apellidos"));
        p.setDni(request.getParameter("dni"));
        p.setTelefono(request.getParameter("telefono"));
        p.setCorreo(request.getParameter("correo"));
        p.setFecha_nacimiento(request.getParameter("fecha_nacimiento"));
        p.setEstado(1);

        boolean ok = pDao.update(p);
        if (p.getId_persona() == 0) {
            // Es nuevo, pero update no funciona para insertar
            // Para simplificar, usamos insert desde AuthController
            ok = false;
        }
        response.getWriter().print(gson.toJson(ok));
    }

    private void editarPersona(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Persona p = new Persona();
        p.setId_persona(Integer.parseInt(request.getParameter("id_persona")));
        p.setNombre(request.getParameter("nombre"));
        p.setApellidos(request.getParameter("apellidos"));
        p.setDni(request.getParameter("dni"));
        p.setTelefono(request.getParameter("telefono"));
        p.setCorreo(request.getParameter("correo"));
        p.setFecha_nacimiento(request.getParameter("fecha_nacimiento"));

        boolean ok = pDao.update(p);
        response.getWriter().print(gson.toJson(ok));
    }

    private void eliminarPersona(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_persona"));
        boolean ok = pDao.delete(id);
        response.getWriter().print(gson.toJson(ok));
    }

    private void buscarPersona(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_persona"));
        Persona p = pDao.searchById(id);
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
        return "PersonaController";
    }

}
