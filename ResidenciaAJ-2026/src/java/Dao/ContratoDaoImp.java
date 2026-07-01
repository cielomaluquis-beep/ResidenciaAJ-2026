package Dao;

import Interface.IContrato;
import Model.Contrato;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Util.ConexionSingleton;

public class ContratoDaoImp implements IContrato {
    @Override
    public int insert(Contrato contrato) {
        int id = 0;
        String sql = "INSERT INTO contrato (fecha_inicio, fecha_fin, estado, id_tipoContrato, id_cliente, id_habitacion, id_usuario) VALUES (?, ?, '1', ?, ?, ?, ?)";
        try {
            Connection conn = ConexionSingleton.getConnection();
            String[] returnId = { "id_contrato" };
            PreparedStatement ps = conn.prepareStatement(sql, returnId);
            ps.setDate(1, contrato.getFecha_inicio());
            ps.setDate(2, contrato.getFecha_fin());
            ps.setInt(3, contrato.getId_tipoContrato());
            ps.setInt(4, contrato.getId_cliente());
            ps.setInt(5, contrato.getId_habitacion());
            ps.setInt(6, contrato.getId_usuario());

            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                    // Actualizar el estado de la habitacion a Ocupada
                    actualizarEstadoHabitacion(conn, contrato.getId_habitacion(), "Ocupado");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public boolean update(Contrato contrato) {
        boolean flag = false;
        String sql = "UPDATE contrato SET fecha_inicio=?, fecha_fin=?, id_tipoContrato=?, id_cliente=?, id_habitacion=? WHERE id_contrato=?";
        try {
            Connection conn = ConexionSingleton.getConnection();
            
            // Si la habitacion cambió, habría que manejar el estado de ambas, pero simplificaremos
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, contrato.getFecha_inicio());
            ps.setDate(2, contrato.getFecha_fin());
            ps.setInt(3, contrato.getId_tipoContrato());
            ps.setInt(4, contrato.getId_cliente());
            ps.setInt(5, contrato.getId_habitacion());
            ps.setInt(6, contrato.getId_contrato());

            if (ps.executeUpdate() > 0) {
                flag = true;
                actualizarEstadoHabitacion(conn, contrato.getId_habitacion(), "Ocupado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean delete(int id) {
        boolean flag = false;
        String sql = "UPDATE contrato SET estado = '0' WHERE id_contrato = ?";
        try {
            Connection conn = ConexionSingleton.getConnection();
            
            // Necesitamos saber qué habitación tenía para liberarla
            Contrato c = buscar(id);
            if(c != null) {
                actualizarEstadoHabitacion(conn, c.getId_habitacion(), "Disponible"); // Disponible
            }
            
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

    @Override
    public List<Contrato> listar() {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT c.*, tc.nombre AS tipoNombre, cl.nombre AS cNombre, cl.ap_paterno, cl.ap_materno, h.numero AS habNumero, u.nombre AS uNombre "
                   + "FROM contrato c "
                   + "INNER JOIN tipo_contrato tc ON c.id_tipoContrato = tc.id_tipoContrato "
                   + "INNER JOIN clientes cl ON c.id_cliente = cl.id_cliente "
                   + "INNER JOIN habitaciones h ON c.id_habitacion = h.id_habitacion "
                   + "INNER JOIN usuarios u ON c.id_usuario = u.id_usuario "
                   + "ORDER BY c.id_contrato DESC";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Contrato c = new Contrato();
                c.setId_contrato(rs.getInt("id_contrato"));
                c.setFecha_inicio(rs.getDate("fecha_inicio"));
                c.setFecha_fin(rs.getDate("fecha_fin"));
                c.setEstado(rs.getString("estado"));
                c.setId_tipoContrato(rs.getInt("id_tipoContrato"));
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setId_habitacion(rs.getInt("id_habitacion"));
                c.setId_usuario(rs.getInt("id_usuario"));

                // Campos extras (transient)
                c.setTipoContratoNombre(rs.getString("tipoNombre"));
                String apMat = rs.getString("ap_materno");
                c.setClienteNombreCompleto(rs.getString("cNombre") + " " + rs.getString("ap_paterno") + " " + (apMat != null ? apMat : ""));
                c.setHabitacionNumero(rs.getString("habNumero"));
                c.setUsuarioNombre(rs.getString("uNombre"));

                lista.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Contrato buscar(int id) {
        Contrato c = null;
        String sql = "SELECT * FROM contrato WHERE id_contrato = ?";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new Contrato();
                c.setId_contrato(rs.getInt("id_contrato"));
                c.setFecha_inicio(rs.getDate("fecha_inicio"));
                c.setFecha_fin(rs.getDate("fecha_fin"));
                c.setEstado(rs.getString("estado"));
                c.setId_tipoContrato(rs.getInt("id_tipoContrato"));
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setId_habitacion(rs.getInt("id_habitacion"));
                c.setId_usuario(rs.getInt("id_usuario"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    
    @Override
    public boolean actualizarEstado(int id_contrato, String estado) {
        boolean flag = false;
        String sql = "UPDATE contrato SET estado=? WHERE id_contrato=?";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id_contrato);

            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private void actualizarEstadoHabitacion(Connection conn, int idHabitacion, String nuevoEstado) {
        String sql = "UPDATE habitaciones SET estado = ? WHERE id_habitacion = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idHabitacion);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
