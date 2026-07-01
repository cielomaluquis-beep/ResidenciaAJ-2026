package Controllers;

import Dao.ContratoDaoImp;
import Dao.TipoContratoDaoImp;
import Dao.ClienteDaoImp;
import Dao.HabitacionDaoImp;
import Interface.IContrato;
import Interface.ITipoContrato;
import Interface.ICliente;
import Interface.IHabitacion;
import Model.Contrato;
import Model.TipoContrato;
import Model.Cliente;
import Model.Habitacion;
import Model.Usuario;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ContratoController", urlPatterns = {"/ContratoController"})
public class ContratoController extends HttpServlet {

    private final IContrato cDao = new ContratoDaoImp();
    private final ITipoContrato tcDao = new TipoContratoDaoImp();
    private final ICliente cliDao = new ClienteDaoImp();
    private final IHabitacion hDao = new HabitacionDaoImp();
    
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "listar":
                List<Contrato> contratos = cDao.listar();
                response.getWriter().print(gson.toJson(contratos));
                break;
            case "listarTipos":
                List<TipoContrato> tipos = tcDao.listar();
                response.getWriter().print(gson.toJson(tipos));
                break;
            case "listarClientes":
                List<Cliente> clientes = cliDao.lista();
                response.getWriter().print(gson.toJson(clientes));
                break;
            case "listarHabitacionesDisponibles":
                // Filtra las habitaciones (acepta '1', 'Disponible', o nulo por defecto)
                List<Habitacion> habitaciones = new java.util.ArrayList<>();
                for (Habitacion h : hDao.lista()) {
                    String est = h.getEstado() != null ? h.getEstado().trim().toLowerCase() : "";
                    if (est.equals("") || est.equals("1") || est.equals("disponible") || est.equals("d")) {
                        habitaciones.add(h);
                    }
                }
                response.getWriter().print(gson.toJson(habitaciones));
                break;
            default:
                response.getWriter().print(gson.toJson("Acción no válida"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");
        
        // Validar sesión para obtener el usuario que realiza la acción
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            response.getWriter().print(gson.toJson(false));
            return;
        }
        Usuario us = (Usuario) sesion.getAttribute("usuario");

        if (action != null) {
            switch (action) {
                case "guardar":
                    guardarContrato(request, response, us.getId_usuario());
                    break;
                case "editar":
                    editarContrato(request, response);
                    break;
                case "eliminar":
                    eliminarContrato(request, response);
                    break;
                default:
                    response.getWriter().print(gson.toJson(false));
            }
        } else {
            response.getWriter().print(gson.toJson(false));
        }
    }

    private void guardarContrato(HttpServletRequest request, HttpServletResponse response, int idUsuario)
            throws IOException {
        try {
            Contrato c = new Contrato();
            c.setFecha_inicio(Date.valueOf(request.getParameter("fecha_inicio")));
            c.setFecha_fin(Date.valueOf(request.getParameter("fecha_fin")));
            c.setId_tipoContrato(Integer.parseInt(request.getParameter("id_tipoContrato")));
            c.setId_cliente(Integer.parseInt(request.getParameter("id_cliente")));
            c.setId_habitacion(Integer.parseInt(request.getParameter("id_habitacion")));
            c.setId_usuario(idUsuario);

            // RN-01: Validar disponibilidad estricta
            Model.Habitacion hab = new Dao.HabitacionDaoImp().searchById(c.getId_habitacion());
            if (hab == null || !"Disponible".equalsIgnoreCase(hab.getEstado())) {
                response.getWriter().print(gson.toJson(false)); // o lanzar un mensaje personalizado si el frontend lo soporta
                return;
            }

            // RN-13: Prohibición de duplicidad de contratos
            java.util.List<Contrato> todos = cDao.listar();
            for (Contrato act : todos) {
                if (act.getId_cliente() == c.getId_cliente() && "Activo".equalsIgnoreCase(act.getEstado())) {
                    response.getWriter().print(gson.toJson(false));
                    return;
                }
            }

            int id = cDao.insert(c);
            response.getWriter().print(gson.toJson(id > 0));
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print(gson.toJson(false));
        }
    }

    private void editarContrato(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Contrato c = new Contrato();
            c.setId_contrato(Integer.parseInt(request.getParameter("id_contrato")));
            c.setFecha_inicio(Date.valueOf(request.getParameter("fecha_inicio")));
            c.setFecha_fin(Date.valueOf(request.getParameter("fecha_fin")));
            c.setId_tipoContrato(Integer.parseInt(request.getParameter("id_tipoContrato")));
            c.setId_cliente(Integer.parseInt(request.getParameter("id_cliente")));
            c.setId_habitacion(Integer.parseInt(request.getParameter("id_habitacion")));

            boolean ok = cDao.update(c);
            response.getWriter().print(gson.toJson(ok));
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print(gson.toJson(false));
        }
    }

    private void eliminarContrato(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id_contrato"));
        boolean ok = cDao.delete(id);
        response.getWriter().print(gson.toJson(ok));
    }
}
