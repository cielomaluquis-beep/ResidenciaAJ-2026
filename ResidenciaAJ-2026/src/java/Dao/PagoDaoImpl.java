package Dao;

import Interface.IPago;
import Model.Pago;
import Util.ConexionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PagoDaoImpl implements IPago {

    @Override
    public List<Pago> listar() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT p.*, "
                   + "COALESCE(cli_c.nombre, cli_r.nombre) AS cli_nombre, "
                   + "COALESCE(cli_c.ap_paterno, cli_r.ap_paterno) AS cli_ap_paterno, "
                   + "COALESCE(h_c.numero, h_r.numero) AS hab_numero, "
                   + "u.nombre AS u_nombre "
                   + "FROM pagos p "
                   + "LEFT JOIN contrato c ON p.id_contrato = c.id_contrato "
                   + "LEFT JOIN reserva r ON p.id_reserva = r.id_reserva "
                   + "LEFT JOIN clientes cli_c ON c.id_cliente = cli_c.id_cliente "
                   + "LEFT JOIN clientes cli_r ON r.id_cliente = cli_r.id_cliente "
                   + "LEFT JOIN habitaciones h_c ON c.id_habitacion = h_c.id_habitacion "
                   + "LEFT JOIN habitaciones h_r ON r.id_habitacion = h_r.id_habitacion "
                   + "LEFT JOIN usuarios u ON p.id_usuario = u.id_usuario "
                   + "ORDER BY p.id_pago DESC";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pago p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setMonto(rs.getDouble("monto"));
                p.setFecha(rs.getDate("fecha"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setConcepto(rs.getString("concepto"));
                p.setImg_comprobante(rs.getString("img_comprobante"));
                p.setEstado(rs.getString("estado"));
                
                int idContrato = rs.getInt("id_contrato");
                if (!rs.wasNull()) p.setId_contrato(idContrato);
                
                int idReserva = rs.getInt("id_reserva");
                if (!rs.wasNull()) p.setId_reserva(idReserva);
                
                p.setId_usuario(rs.getInt("id_usuario"));

                // Transients
                String nombre = rs.getString("cli_nombre");
                String ap = rs.getString("cli_ap_paterno");
                p.setClienteNombreCompleto((nombre != null ? nombre : "") + " " + (ap != null ? ap : ""));
                
                p.setNumHabitacion(rs.getString("hab_numero"));
                p.setUsuarioNombre(rs.getString("u_nombre"));
                
                if (p.getId_contrato() > 0) {
                    p.setDetalleAsociado("Contrato #" + p.getId_contrato());
                } else if (p.getId_reserva() > 0) {
                    p.setDetalleAsociado("Reserva #" + p.getId_reserva());
                } else {
                    p.setDetalleAsociado("N/A");
                }

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Pago buscar(int id) {
        Pago p = null;
        String sql = "SELECT * FROM pagos WHERE id_pago = ?";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setMonto(rs.getDouble("monto"));
                p.setFecha(rs.getDate("fecha"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setConcepto(rs.getString("concepto"));
                p.setImg_comprobante(rs.getString("img_comprobante"));
                p.setEstado(rs.getString("estado"));
                
                int idContrato = rs.getInt("id_contrato");
                if (!rs.wasNull()) p.setId_contrato(idContrato);
                
                int idReserva = rs.getInt("id_reserva");
                if (!rs.wasNull()) p.setId_reserva(idReserva);
                
                p.setId_usuario(rs.getInt("id_usuario"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public boolean insert(Pago pago) {
        boolean flag = false;
        String sql = "INSERT INTO pagos (monto, fecha, metodo_pago, concepto, img_comprobante, estado, id_reserva, id_contrato, id_usuario) VALUES (?, ?, ?, ?, ?, '1', ?, ?, ?)";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, pago.getMonto());
            ps.setDate(2, pago.getFecha());
            ps.setString(3, pago.getMetodo_pago());
            ps.setString(4, pago.getConcepto());
            ps.setString(5, pago.getImg_comprobante());
            
            if (pago.getId_reserva() > 0) {
                ps.setInt(6, pago.getId_reserva());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            
            if (pago.getId_contrato() > 0) {
                ps.setInt(7, pago.getId_contrato());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            
            ps.setInt(8, pago.getId_usuario());

            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean anular(int id) {
        boolean flag = false;
        String sql = "UPDATE pagos SET estado = '0' WHERE id_pago = ?";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
