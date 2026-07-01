package Controllers;

import Dao.ContratoDaoImp;
import Dao.HabitacionDaoImp;
import Dao.PagoDaoImpl;
import Interface.IContrato;
import Interface.IHabitacion;
import Model.Contrato;
import Model.Habitacion;
import Model.Pago;
import Model.Usuario;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "DashboardController", urlPatterns = {"/DashboardController"})
public class DashboardController extends HttpServlet {

    private Gson gson;

    @Override
    public void init() throws ServletException {
        gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null || u.getId_rol() == 3) {
            out.print("{\"status\":\"error\", \"message\":\"No tienes permisos\"}");
            return;
        }

        IHabitacion hDao = new HabitacionDaoImp();
        IContrato cDao = new ContratoDaoImp();
        PagoDaoImpl pDao = new PagoDaoImpl();

        List<Habitacion> habitaciones = hDao.lista();
        List<Contrato> contratos = cDao.listar();
        List<Pago> pagos = pDao.listar(); // Asumiendo q existe o crearemos

        int habOcupadas = 0;
        int habTotales = habitaciones.size();
        for (Habitacion h : habitaciones) {
            if ("Ocupado".equalsIgnoreCase(h.getEstado())) habOcupadas++;
        }

        int contActivos = 0;
        for (Contrato c : contratos) {
            if ("Activo".equalsIgnoreCase(c.getEstado()) || "1".equals(c.getEstado())) contActivos++;
        }

        double ingresosMes = 0.0;
        for (Pago p : pagos) {
            // Logica simple, asumiendo todos los pagos son del mes
            ingresosMes += p.getMonto();
        }

        String json = String.format("{\"habOcupadas\":%d, \"habTotales\":%d, \"contActivos\":%d, \"ingresosMes\":%.2f}", 
                                     habOcupadas, habTotales, contActivos, ingresosMes);
        out.print(json);
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
