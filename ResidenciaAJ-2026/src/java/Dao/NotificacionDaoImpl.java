package Dao;

import Model.Notificacion;
import Util.ConexionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Interface.INotificacion;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDaoImpl implements INotificacion {
    
    private Connection conn;

    public NotificacionDaoImpl() {
        conn = ConexionSingleton.getConnection();
    }

    @Override
    public boolean insertar(Notificacion n) {
        String sql = "INSERT INTO notificaciones (tipo, mensaje, id_usuario, url_destino) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, n.getTipo());
            ps.setString(2, n.getMensaje());
            ps.setInt(3, n.getIdUsuario());
            ps.setString(4, n.getUrlDestino());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Notificacion> listarPorUsuario(int id_usuario) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT id_notificacion, tipo, mensaje, fecha_creacion, leido, id_usuario, url_destino FROM notificaciones WHERE id_usuario = ? ORDER BY id_notificacion DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notificacion n = new Notificacion();
                n.setIdNotificacion(rs.getInt("id_notificacion"));
                n.setTipo(rs.getString("tipo"));
                n.setMensaje(rs.getString("mensaje"));
                n.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                n.setLeido(rs.getInt("leido"));
                n.setIdUsuario(rs.getInt("id_usuario"));
                n.setUrlDestino(rs.getString("url_destino"));
                lista.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean marcarLeida(int id_notificacion, int id_usuario) {
        String sql = "UPDATE notificaciones SET leido = 1 WHERE id_notificacion = ? AND id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_notificacion);
            ps.setInt(2, id_usuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean marcarTodasLeidas(int id_usuario) {
        String sql = "UPDATE notificaciones SET leido = 1 WHERE id_usuario = ? AND leido = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_usuario);
            return ps.executeUpdate() >= 0; // Return true even if 0 updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id_notificacion, int id_usuario) {
        String sql = "DELETE FROM notificaciones WHERE id_notificacion = ? AND id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_notificacion);
            ps.setInt(2, id_usuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
