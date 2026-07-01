package Dao;

import Interface.IIncidencia;
import Model.Incidencia;
import Util.ConexionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDaoImpl implements IIncidencia {

    @Override
    public List<Incidencia> listar() {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre as u_nombre, h.numero as h_numero "
                   + "FROM incidencias i "
                   + "INNER JOIN usuarios u ON i.id_usuario = u.id_usuario "
                   + "INNER JOIN habitaciones h ON i.id_habitacion = h.id_habitacion "
                   + "ORDER BY i.id_incidencia DESC";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setId_incidencia(rs.getInt("id_incidencia"));
                i.setTipo_falla(rs.getString("tipo_falla"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setFoto(rs.getString("foto"));
                i.setFecha_reporte(rs.getTimestamp("fecha_reporte"));
                i.setFecha_resolucion(rs.getTimestamp("fecha_resolucion"));
                i.setEstado(rs.getString("estado"));
                i.setId_habitacion(rs.getInt("id_habitacion"));
                i.setId_usuario(rs.getInt("id_usuario"));
                
                i.setNombre_estudiante(rs.getString("u_nombre"));
                i.setNumero_habitacion(rs.getString("h_numero"));
                lista.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Incidencia> listarPorUsuario(int id_usuario) {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT i.*, u.nombre as u_nombre, h.numero as h_numero "
                   + "FROM incidencias i "
                   + "INNER JOIN usuarios u ON i.id_usuario = u.id_usuario "
                   + "INNER JOIN habitaciones h ON i.id_habitacion = h.id_habitacion "
                   + "WHERE i.id_usuario = ? "
                   + "ORDER BY i.id_incidencia DESC";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Incidencia i = new Incidencia();
                i.setId_incidencia(rs.getInt("id_incidencia"));
                i.setTipo_falla(rs.getString("tipo_falla"));
                i.setDescripcion(rs.getString("descripcion"));
                i.setFoto(rs.getString("foto"));
                i.setFecha_reporte(rs.getTimestamp("fecha_reporte"));
                i.setFecha_resolucion(rs.getTimestamp("fecha_resolucion"));
                i.setEstado(rs.getString("estado"));
                i.setId_habitacion(rs.getInt("id_habitacion"));
                i.setId_usuario(rs.getInt("id_usuario"));
                
                i.setNombre_estudiante(rs.getString("u_nombre"));
                i.setNumero_habitacion(rs.getString("h_numero"));
                lista.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean insert(Incidencia incidencia) {
        boolean flag = false;
        String sql = "INSERT INTO incidencias (tipo_falla, descripcion, foto, estado, id_habitacion, id_usuario) VALUES (?, ?, ?, 'Pendiente', ?, ?)";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, incidencia.getTipo_falla());
            ps.setString(2, incidencia.getDescripcion());
            ps.setString(3, incidencia.getFoto());
            ps.setInt(4, incidencia.getId_habitacion());
            ps.setInt(5, incidencia.getId_usuario());
            
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean actualizarEstado(int id_incidencia, String estado) {
        boolean flag = false;
        String sql = "";
        if (estado.equalsIgnoreCase("Resuelto")) {
            sql = "UPDATE incidencias SET estado = ?, fecha_resolucion = CURRENT_TIMESTAMP WHERE id_incidencia = ?";
        } else {
            sql = "UPDATE incidencias SET estado = ?, fecha_resolucion = NULL WHERE id_incidencia = ?";
        }
        
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, id_incidencia);
            
            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
