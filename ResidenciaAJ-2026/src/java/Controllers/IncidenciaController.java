package Controllers;

import Dao.IncidenciaDaoImpl;
import Dao.NotificacionDaoImpl;
import Dao.ReservaDaoImpl;
import Interface.IIncidencia;
import Interface.INotificacion;
import Model.Incidencia;
import Model.Notificacion;
import Model.Reserva;
import Model.Usuario;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Timestamp;
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

@WebServlet(name = "IncidenciaController", urlPatterns = {"/IncidenciaController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class IncidenciaController extends HttpServlet {

    private IIncidencia iDao;
    private INotificacion nDao;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        iDao = new IncidenciaDaoImpl();
        nDao = new NotificacionDaoImpl();
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
            case "listarTodas":
                if (u.getId_rol() == 1 || u.getId_rol() == 2) {
                    List<Incidencia> lista = iDao.listar();
                    out.print(gson.toJson(lista));
                } else {
                    out.print("{\"status\":\"error\", \"message\":\"No tienes permisos\"}");
                }
                break;
            case "listarMisIncidencias":
                if (u.getId_rol() == 3) {
                    List<Incidencia> misIncidencias = iDao.listarPorUsuario(u.getId_usuario());
                    out.print(gson.toJson(misIncidencias));
                } else {
                    out.print("[]");
                }
                break;
            case "reportar":
                if (u.getId_rol() == 3) {
                    String tipoFalla = request.getParameter("tipoFalla");
                    String descripcion = request.getParameter("descripcion");
                    
                    if (descripcion == null || descripcion.trim().isEmpty()) {
                        out.print("{\"status\":\"error\", \"message\":\"Debes describir el problema antes de enviar\"}");
                        return;
                    }
                    
                    // Obtener la habitacion a reportar
                    String idHabStr = request.getParameter("id_habitacion");
                    int idHabitacion = -1;
                    if (idHabStr != null && !idHabStr.isEmpty()) {
                        try {
                            idHabitacion = Integer.parseInt(idHabStr);
                        } catch (NumberFormatException e) {
                        }
                    }

                    if (idHabitacion == -1) {
                        // Fallback a buscar reserva activa
                        ReservaDaoImpl rDao = new ReservaDaoImpl();
                        List<Reserva> reservas = rDao.listarPorUsuario(u.getId_usuario());
                        for (Reserva r : reservas) {
                            if (r.getEstado().equals("1") || r.getEstado().equals("2") || r.getEstado().equals("Activo")) {
                                idHabitacion = r.getId_habitacion();
                                break;
                            }
                        }
                    }
                    
                    if (idHabitacion == -1) {
                        out.print("{\"status\":\"error\", \"message\":\"No se pudo determinar la habitacion a reportar.\"}");
                        return;
                    }

                    // Upload photo
                    Part part = request.getPart("foto");
                    String fileName = null;
                    if (part != null && part.getSize() > 0) {
                        String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "img" + File.separator + "incidencias";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) uploadDir.mkdirs();

                        String originalFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                        String extension = "";
                        int i = originalFileName.lastIndexOf('.');
                        if (i > 0) extension = originalFileName.substring(i);

                        fileName = UUID.randomUUID().toString() + extension;
                        part.write(uploadPath + File.separator + fileName);
                    }

                    Incidencia in = new Incidencia();
                    in.setTipo_falla(tipoFalla);
                    in.setDescripcion(descripcion);
                    in.setFoto(fileName);
                    in.setId_habitacion(idHabitacion);
                    in.setId_usuario(u.getId_usuario());

                    if (iDao.insert(in)) {
                        // Notificar a la administradora (rol = 2)
                        // Para simplificar, insertamos notificacion general o para un usuario especifico.
                        // En la logica de Negocio, esto deberia alertar a Administradora.
                        out.print("{\"status\":\"success\", \"message\":\"Reporte enviado exitosamente.\"}");
                    } else {
                        out.print("{\"status\":\"error\", \"message\":\"Error al guardar el reporte.\"}");
                    }
                } else {
                    out.print("{\"status\":\"error\", \"message\":\"Solo estudiantes pueden reportar incidencias.\"}");
                }
                break;
            case "actualizarEstado":
                if (u.getId_rol() == 1 || u.getId_rol() == 2) {
                    int id_incidencia = Integer.parseInt(request.getParameter("id_incidencia"));
                    String nuevoEstado = request.getParameter("estado"); // Pendiente, En proceso, Resuelto
                    
                    if (iDao.actualizarEstado(id_incidencia, nuevoEstado)) {
                        out.print("{\"status\":\"success\", \"message\":\"Estado actualizado correctamente.\"}");
                    } else {
                        out.print("{\"status\":\"error\", \"message\":\"No se pudo actualizar el estado.\"}");
                    }
                } else {
                    out.print("{\"status\":\"error\", \"message\":\"Sin permisos\"}");
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
