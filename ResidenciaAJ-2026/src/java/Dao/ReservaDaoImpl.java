package Dao;

import Interface.IReserva;
import Model.Reserva;
import Util.ConexionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReservaDaoImpl implements IReserva {

    private Connection cn;

    @Override
    public int insertar(Reserva r) {
        int idGenerado = 0;
        String sql = "INSERT INTO reserva (fecha_reserva, fecha_posibleIni, estado, id_cliente, id_habitacion, id_usuario) VALUES (?, ?, '1', ?, ?, ?)";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(sql, new String[]{"id_reserva"});
            st.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            st.setDate(2, r.getFecha_posibleIni());
            st.setInt(3, r.getId_cliente());
            st.setInt(4, r.getId_habitacion());
            st.setInt(5, r.getId_usuario());
            
            if (st.executeUpdate() > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar reserva: " + e.getMessage());
        }
        return idGenerado;
    }

    @Override
    public List<Reserva> listarPorUsuario(int id_usuario) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reserva WHERE id_usuario = ? ORDER BY id_reserva DESC";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(sql);
            st.setInt(1, id_usuario);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId_reserva(rs.getInt("id_reserva"));
                r.setFecha_reserva(rs.getTimestamp("fecha_reserva"));
                r.setFecha_posibleIni(rs.getDate("fecha_posibleIni"));
                r.setEstado(rs.getString("estado"));
                r.setId_cliente(rs.getInt("id_cliente"));
                r.setId_habitacion(rs.getInt("id_habitacion"));
                r.setId_usuario(rs.getInt("id_usuario"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error listarPorUsuario reserva: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> listarTodas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, c.nombre as cli_nom, c.ap_paterno as cli_ap, h.numero as hab_num, p.monto as p_monto, p.img_comprobante as p_img " +
                     "FROM reserva r " +
                     "LEFT JOIN clientes c ON r.id_cliente = c.id_cliente " +
                     "LEFT JOIN habitaciones h ON r.id_habitacion = h.id_habitacion " +
                     "LEFT JOIN pagos p ON r.id_reserva = p.id_reserva " +
                     "ORDER BY r.id_reserva DESC";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId_reserva(rs.getInt("id_reserva"));
                r.setFecha_reserva(rs.getTimestamp("fecha_reserva"));
                r.setFecha_posibleIni(rs.getDate("fecha_posibleIni"));
                r.setEstado(rs.getString("estado"));
                r.setId_cliente(rs.getInt("id_cliente"));
                r.setId_habitacion(rs.getInt("id_habitacion"));
                r.setId_usuario(rs.getInt("id_usuario"));
                
                String nom = rs.getString("cli_nom") != null ? rs.getString("cli_nom") : "";
                String ap = rs.getString("cli_ap") != null ? rs.getString("cli_ap") : "";
                r.setClienteNombre(nom + " " + ap);
                r.setHabitacionNumero(rs.getString("hab_num"));
                
                r.setPagoMonto(rs.getDouble("p_monto"));
                r.setPagoComprobante(rs.getString("p_img"));
                
                lista.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error listarTodas reserva: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizarEstado(int id_reserva, String estado) {
        String sql = "UPDATE reserva SET estado = ? WHERE id_reserva = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(sql);
            st.setString(1, estado);
            st.setInt(2, id_reserva);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizarEstado reserva: " + e.getMessage());
            return false;
        }
    }
}
