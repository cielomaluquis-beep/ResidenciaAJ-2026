package Dao;

import Interface.IHabitacion;
import Model.Habitacion;
import Util.ConexionSingleton;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class HabitacionDaoImp implements IHabitacion {
    
    private Connection cn;

    @Override
    public List<Habitacion> lista() {
        List<Habitacion> lista = new ArrayList<>();
        String query = "SELECT * FROM habitaciones";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Habitacion h = new Habitacion();
                h.setId_habitacion(rs.getInt("id_habitacion"));
                h.setNumero(rs.getString("numero"));
                h.setTipo(rs.getString("tipo"));
                h.setPrecio(rs.getDouble("precio"));
                h.setPiso(rs.getInt("piso"));
                h.setTipo_bano(rs.getString("tipo_bano"));
                h.setImg_habitacion(rs.getString("img_habitacion"));
                h.setEstado(rs.getString("estado"));
                lista.add(h);
            }
        } catch (SQLException e) {
            System.out.println("Error lista habitaciones: " + e.getMessage());
        }
        return lista;    
    }

    @Override
    public int insert(Habitacion h) {
        String query = "INSERT INTO habitaciones (numero, tipo, precio, piso, tipo_bano, img_habitacion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int resultado = 0;
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, h.getNumero());
            st.setString(2, h.getTipo());
            st.setDouble(3, h.getPrecio());
            st.setInt(4, h.getPiso());
            st.setString(5, h.getTipo_bano());
            st.setString(6, h.getImg_habitacion());
            st.setString(7, h.getEstado());
            resultado = st.executeUpdate();
            if (resultado > 0) {
                // Oracle might not return generated keys properly with this exact syntax, but we leave it as is
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    h.setId_habitacion(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar habitacion: " + e.getMessage());
        }
        return resultado;    
    }

    @Override
    public boolean update(Habitacion h) {
        String query = "UPDATE habitaciones SET numero=?, tipo=?, precio=?, piso=?, tipo_bano=?, img_habitacion=?, estado=? WHERE id_habitacion=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, h.getNumero());
            st.setString(2, h.getTipo());
            st.setDouble(3, h.getPrecio());
            st.setInt(4, h.getPiso());
            st.setString(5, h.getTipo_bano());
            st.setString(6, h.getImg_habitacion());
            st.setString(7, h.getEstado());
            st.setInt(8, h.getId_habitacion());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error update habitacion: " + e.getMessage());
        }
        return false;    
    }

    @Override
    public Habitacion searchById(int id) {
        Habitacion h = null;
        String query = "SELECT * FROM habitaciones WHERE id_habitacion = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                h = new Habitacion();
                h.setId_habitacion(rs.getInt("id_habitacion"));
                h.setNumero(rs.getString("numero"));
                h.setTipo(rs.getString("tipo"));
                h.setPrecio(rs.getDouble("precio"));
                h.setPiso(rs.getInt("piso"));
                h.setTipo_bano(rs.getString("tipo_bano"));
                h.setImg_habitacion(rs.getString("img_habitacion"));
                h.setEstado(rs.getString("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error buscar habitacion: " + e.getMessage());
        }
        return h;    
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM habitaciones WHERE id_habitacion = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error delete habitacion: " + e.getMessage());
        }
        return false;
    }
}
