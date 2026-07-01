package Controllers;

import Dao.ClienteDaoImp;
import Interface.ICliente;
import Model.Cliente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ClienteController", urlPatterns = {"/ClienteController"})
public class ClienteController extends HttpServlet {

    private final ICliente cDao = new ClienteDaoImp();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

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
                guardarCliente(request, response);
                break;
            case "editar":
                editarCliente(request, response);
                break;
            case "eliminar":
                eliminarCliente(request, response);
                break;
            case "buscar":
                buscarCliente(request, response);
                break;
            default:
                listarClientes(request, response);
                break;
        }
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Cliente> clientes = cDao.lista();
        response.getWriter().print(gson.toJson(clientes));
    }

    private void guardarCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Cliente c = new Cliente();
        c.setNombre(request.getParameter("nombre"));
        c.setAp_paterno(request.getParameter("ap_paterno"));
        c.setAp_materno(request.getParameter("ap_materno"));
        c.setNro_documento(request.getParameter("nro_documento"));
        c.setTelefono(request.getParameter("telefono"));
        c.setEmail(request.getParameter("email"));
        
        String fecha = request.getParameter("fecha_nac");
        if(fecha != null && !fecha.isEmpty()) {
            c.setFecha_nac(Date.valueOf(fecha));
        }

        int id = cDao.insert(c);
        response.getWriter().print(gson.toJson(id > 0));
    }

    private void editarCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Cliente c = new Cliente();
        c.setId_cliente(Integer.parseInt(request.getParameter("id_cliente")));
        c.setNombre(request.getParameter("nombre"));
        c.setAp_paterno(request.getParameter("ap_paterno"));
        c.setAp_materno(request.getParameter("ap_materno"));
        c.setNro_documento(request.getParameter("nro_documento"));
        c.setTelefono(request.getParameter("telefono"));
        c.setEmail(request.getParameter("email"));
        
        String fecha = request.getParameter("fecha_nac");
        if(fecha != null && !fecha.isEmpty()) {
            c.setFecha_nac(Date.valueOf(fecha));
        }

        boolean ok = cDao.update(c);
        response.getWriter().print(gson.toJson(ok));
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_cliente"));
        boolean ok = cDao.delete(id);
        response.getWriter().print(gson.toJson(ok));
    }

    private void buscarCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_cliente"));
        Cliente c = cDao.searchById(id);
        response.getWriter().print(gson.toJson(c));
    }
}
