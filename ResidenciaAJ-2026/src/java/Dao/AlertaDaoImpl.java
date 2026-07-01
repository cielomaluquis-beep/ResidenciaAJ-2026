package Dao;

import Interface.IAlerta;
import Model.Alerta;
import Util.ConexionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AlertaDaoImpl implements IAlerta {

    @Override
    public List<Alerta> listarPorUsuario(int id_usuario) {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT * FROM alertas WHERE id_usuario = ? ORDER BY id_alerta DESC";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Alerta a = new Alerta();
                a.setId_alerta(rs.getInt("id_alerta"));
                a.setTipo_alerta(rs.getString("tipo_alerta"));
                a.setMensaje(rs.getString("mensaje"));
                a.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                a.setLeida(rs.getString("leida"));
                a.setId_usuario(rs.getInt("id_usuario"));
                lista.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean insert(Alerta alerta) {
        boolean flag = false;
        String sql = "INSERT INTO alertas (tipo_alerta, mensaje, leida, id_usuario) VALUES (?, ?, '0', ?)";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, alerta.getTipo_alerta());
            ps.setString(2, alerta.getMensaje());
            ps.setInt(3, alerta.getId_usuario());
            
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean marcarLeida(int id_alerta) {
        boolean flag = false;
        String sql = "UPDATE alertas SET leida = '1' WHERE id_alerta = ?";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id_alerta);
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean marcarTodasLeidas(int id_usuario) {
        boolean flag = false;
        String sql = "UPDATE alertas SET leida = '1' WHERE id_usuario = ?";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id_usuario);
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public int contarNoLeidas(int id_usuario) {
        int count = 0;
        String sql = "SELECT COUNT(*) as total FROM alertas WHERE id_usuario = ? AND leida = '0'";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public boolean existeAlertaReciente(int id_usuario, String tipo_alerta, String mensaje) {
        boolean existe = false;
        // Obtenemos todas las alertas de este tipo y mensaje para el usuario
        String sql = "SELECT fecha_creacion FROM alertas WHERE id_usuario = ? AND tipo_alerta = ? AND mensaje = ? ORDER BY id_alerta DESC";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id_usuario);
            ps.setString(2, tipo_alerta);
            ps.setString(3, mensaje);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Hay al menos una, ver si fue hace menos de 24 horas
                java.sql.Timestamp fecha = rs.getTimestamp("fecha_creacion");
                long diff = System.currentTimeMillis() - fecha.getTime();
                if (diff < (24 * 60 * 60 * 1000)) { // Menos de 24 horas
                    existe = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existe;
    }
}
