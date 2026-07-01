package Controllers;

import Dao.PagoDaoImpl;
import Interface.IPago;
import Model.Pago;
import Model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "PagoController", urlPatterns = {"/PagoController"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class PagoController extends HttpServlet {

    private final IPago pagoDao = new PagoDaoImpl();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");

        try (PrintWriter out = response.getWriter()) {
            if ("listar".equals(action)) {
                List<Pago> lista = pagoDao.listar();
                out.print(gson.toJson(lista));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String action = request.getParameter("action");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Usuario u = (Usuario) session.getAttribute("usuario");

        try (PrintWriter out = response.getWriter()) {
            if ("guardar".equals(action)) {
                double monto = Double.parseDouble(request.getParameter("monto"));
                // RN-09: El monto debe ser mayor a 0
                if (monto <= 0) {
                    out.print("{\"status\":\"error\", \"message\":\"El monto debe ser mayor a cero\"}");
                    return;
                }
                Date fecha = Date.valueOf(request.getParameter("fecha"));
                String metodo_pago = request.getParameter("metodo_pago");
                String concepto = request.getParameter("concepto");
                
                String idContratoStr = request.getParameter("id_contrato");
                int idContrato = (idContratoStr != null && !idContratoStr.isEmpty()) ? Integer.parseInt(idContratoStr) : 0;
                
                String idReservaStr = request.getParameter("id_reserva");
                int idReserva = (idReservaStr != null && !idReservaStr.isEmpty()) ? Integer.parseInt(idReservaStr) : 0;

                String imgName = "";
                Part part = request.getPart("img_comprobante");
                if (part != null && part.getSize() > 0) {
                    String fileName = extractFileName(part);
                    String extension = fileName.substring(fileName.lastIndexOf("."));
                    imgName = UUID.randomUUID().toString() + extension;
                    
                    String uploadPath = request.getServletContext().getRealPath("") + File.separator + "comprobantes";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdir();
                    part.write(uploadPath + File.separator + imgName);
                }

                Pago p = new Pago();
                p.setMonto(monto);
                p.setFecha(fecha);
                p.setMetodo_pago(metodo_pago);
                p.setConcepto(concepto);
                p.setImg_comprobante(imgName);
                p.setId_contrato(idContrato);
                p.setId_reserva(idReserva);
                p.setId_usuario(u.getId_usuario());

                boolean ok = pagoDao.insert(p);
                out.print(gson.toJson(ok));
                
            } else if ("anular".equals(action)) {
                int id_pago = Integer.parseInt(request.getParameter("id_pago"));
                boolean ok = pagoDao.anular(id_pago);
                out.print(gson.toJson(ok));
            }
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}
