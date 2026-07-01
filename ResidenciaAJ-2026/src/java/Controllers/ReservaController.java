package Controllers;

import Dao.ClienteDaoImp;
import Dao.PagoDaoImpl;
import Dao.ReservaDaoImpl;
import Interface.ICliente;
import Interface.IPago;
import Interface.IReserva;
import Model.Cliente;
import Model.Pago;
import Model.Reserva;
import Model.Usuario;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(name = "ReservaController", urlPatterns = {"/ReservaController"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ReservaController extends HttpServlet {

    private IReserva rDao;
    private ICliente cDao;
    private IPago pDao;

    @Override
    public void init() throws ServletException {
        rDao = new ReservaDaoImpl();
        cDao = new ClienteDaoImp();
        pDao = new PagoDaoImpl();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        if (action == null) {
            out.print("{\"status\":\"error\", \"message\":\"No action specified\"}");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.print("{\"status\":\"error\", \"message\":\"Usuario no autenticado\"}");
            return;
        }
        Usuario u = (Usuario) session.getAttribute("usuario");

        Gson gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        switch (action) {
            case "insertar":
                try {
                    String idHabitacionStr = request.getParameter("id_habitacion");
                    String fechaPosible = request.getParameter("fecha_posibleIni");
                    String montoStr = request.getParameter("monto");
                    
                    if (idHabitacionStr == null || fechaPosible == null || montoStr == null) {
                        out.print("{\"status\":\"error\", \"message\":\"Datos incompletos\"}");
                        return;
                    }
                    
                    int idHabitacion = Integer.parseInt(idHabitacionStr);
                    double monto = Double.parseDouble(montoStr);
                    Date fechaPosibleIni = Date.valueOf(fechaPosible);
                    
                    // Manejar imagen comprobante
                    Part part = request.getPart("comprobante");
                    String fileName = null;
                    if (part != null && part.getSize() > 0) {
                        String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img" + File.separator + "comprobantes";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) uploadDir.mkdirs();
                        
                        String originalFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                        String extension = "";
                        int i = originalFileName.lastIndexOf('.');
                        if (i > 0) extension = originalFileName.substring(i);
                        
                        fileName = UUID.randomUUID().toString() + extension;
                        part.write(uploadPath + File.separator + fileName);
                    } else {
                        out.print("{\"status\":\"error\", \"message\":\"Debe adjuntar un comprobante de pago\"}");
                        return;
                    }
                    
                    // Buscar ID de Cliente asociado al Usuario
                    Cliente cliente = cDao.searchByEmail(u.getEmail());
                    if (cliente == null) {
                        out.print("{\"status\":\"error\", \"message\":\"No se encontró información de cliente para tu usuario.\"}");
                        return;
                    }
                    
                    
                    Model.Habitacion hab = new Dao.HabitacionDaoImp().searchById(idHabitacion);
                    if (hab == null || !"Disponible".equalsIgnoreCase(hab.getEstado())) {
                        out.print("{\"status\":\"error\", \"message\":\"La habitación ya no se encuentra disponible.\"}");
                        return;
                    }

                    
                    Reserva r = new Reserva();
                    r.setFecha_posibleIni(fechaPosibleIni);
                    r.setId_cliente(cliente.getId_cliente());
                    r.setId_habitacion(idHabitacion);
                    r.setId_usuario(u.getId_usuario());
                    
                    int idReserva = rDao.insertar(r);
                    
                    if (idReserva > 0) {
                        // Crear Pago del adelanto
                        Pago p = new Pago();
                        p.setMonto(monto);
                        p.setFecha(new Date(System.currentTimeMillis()));
                        p.setMetodo_pago("Transferencia/Yape");
                        p.setConcepto("Adelanto de Reserva #" + idReserva);
                        p.setImg_comprobante(fileName);
                        p.setId_reserva(idReserva);
                        p.setId_usuario(u.getId_usuario());
                        
                        pDao.insert(p);
                        
                        out.print("{\"status\":\"success\"}");
                    } else {
                        out.print("{\"status\":\"error\", \"message\":\"No se pudo crear la reserva.\"}");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.print("{\"status\":\"error\", \"message\":\"Error interno del servidor\"}");
                }
                break;
                
            case "mis_reservas":
                List<Reserva> misReservas = rDao.listarPorUsuario(u.getId_usuario());
                out.print(gson.toJson(misReservas));
                break;
                
            case "listarTodas":
                List<Reserva> todas = rDao.listarTodas();
                out.print(gson.toJson(todas));
                break;
                
            case "actualizarEstado":
                String idRStr = request.getParameter("id_reserva");
                String estado = request.getParameter("estado");
                if (idRStr != null && estado != null) {
                    boolean ok = rDao.actualizarEstado(Integer.parseInt(idRStr), estado);
                    out.print(ok ? "{\"status\":\"success\"}" : "{\"status\":\"error\"}");
                } else {
                    out.print("{\"status\":\"error\", \"message\":\"Faltan parámetros\"}");
                }
                break;
                
            default:
                out.print("{\"status\":\"error\", \"message\":\"Acción inválida\"}");
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
