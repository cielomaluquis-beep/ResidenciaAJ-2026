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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@WebServlet(name = "HabitacionController", urlPatterns = {"/HabitacionController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class HabitacionController extends HttpServlet {

    private final IHabitacion hDao = new HabitacionDaoImp();
    private final Gson gson = new Gson();

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
        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }
        
        if (!"guardar".equals(action) && !"editar".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
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
            case "listarDisponibles":
                listarDisponibles(request, response);
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

    private void listarDisponibles(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Habitacion> habitaciones = hDao.lista();
        habitaciones.removeIf(h -> h.getEstado() == null || !h.getEstado().equalsIgnoreCase("Disponible"));
        response.getWriter().print(gson.toJson(habitaciones));
    }

    private String procesarImagen(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("img_habitacion");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
            
            // Ruta de ejecución (Tomcat) para que se vea inmediatamente
            String uploadPath = getServletContext().getRealPath("/") + "assets" + File.separator + "img" + File.separator + "foto-habitacion";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            File file = new File(uploadDir, fileName);
            
            // Ruta del código fuente (NetBeans) para que no se borre en Clean and Build
            String sourcePath = "c:\\Users\\cielo\\OneDrive\\Documents\\Documentos\\NetBeansProjects\\ResidenciaAJ-2026\\ResidenciaAJ-2026\\web\\assets\\img\\foto-habitacion";
            File sourceDir = new File(sourcePath);
            if (!sourceDir.exists()) sourceDir.mkdirs();
            File sourceFile = new File(sourceDir, fileName);

            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            
           
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, sourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName;
        }
        return null;
    }

    private void guardarHabitacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Habitacion h = new Habitacion();
        h.setNumero(request.getParameter("numero"));
        h.setTipo(request.getParameter("tipo"));
        h.setPrecio(Double.parseDouble(request.getParameter("precio")));
        h.setPiso(Integer.parseInt(request.getParameter("piso")));
        h.setTipo_bano(request.getParameter("tipo_bano"));
        h.setEstado("Disponible");
        
        String img = procesarImagen(request);
        if (img != null) h.setImg_habitacion(img);

        int id = hDao.insert(h);
        response.getWriter().print(gson.toJson(id > 0));
    }

    private void editarHabitacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Habitacion h = new Habitacion();
        h.setId_habitacion(Integer.parseInt(request.getParameter("id_habitacion")));
        h.setNumero(request.getParameter("numero"));
        h.setTipo(request.getParameter("tipo"));
        h.setPrecio(Double.parseDouble(request.getParameter("precio")));
        h.setPiso(Integer.parseInt(request.getParameter("piso")));
        h.setTipo_bano(request.getParameter("tipo_bano"));
        h.setEstado(request.getParameter("estado"));
        
        String img = procesarImagen(request);
        if (img != null) {
            h.setImg_habitacion(img);
        } else {
            // Retrieve old image if not updated
            Habitacion old = hDao.searchById(h.getId_habitacion());
            if (old != null) h.setImg_habitacion(old.getImg_habitacion());
        }

        boolean ok = hDao.update(h);
        response.getWriter().print(gson.toJson(ok));
    }

    private void eliminarHabitacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
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
}
