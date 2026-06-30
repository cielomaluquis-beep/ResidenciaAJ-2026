/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Interface.IHabitacion;
import Model.Habitacion;
import Util.ConexionSingleton;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author cielo
 */
public class HabitacionDaoImp implements IHabitacion{
    
        private Connection cn;


    @Override
    public List<Habitacion> lista() {
List<Habitacion> lista = new ArrayList<>();
        String query = "SELECT * FROM Habitaciones WHERE estado = 1";
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
                h.setEstado(rs.getInt("estado"));
                lista.add(h);
            }
        } catch (SQLException e) {
            System.out.println("Error lista habitaciones: " + e.getMessage());
        }
        return lista;    }

    @Override
    public int insert(Habitacion h) {
        String query = "INSERT INTO Habitaciones (numero, tipo, precio, estado) VALUES (?, ?, ?, ?)";
        int resultado = 0;
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, h.getNumero());
            st.setString(2, h.getTipo());
            st.setDouble(3, h.getPrecio());
            st.setInt(4, h.getEstado());
            resultado = st.executeUpdate();
            if (resultado > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    h.setId_habitacion(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar habitacion: " + e.getMessage());
        }
        return resultado;    }

    @Override
    public boolean update(Habitacion h) {
        String query = "UPDATE Habitaciones SET numero=?, tipo=?, precio=?, estado=? WHERE id_habitacion=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, h.getNumero());
            st.setString(2, h.getTipo());
            st.setDouble(3, h.getPrecio());
            st.setInt(4, h.getEstado());
            st.setInt(5, h.getId_habitacion());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error update habitacion: " + e.getMessage());
        }
        return false;    }

    @Override
    public Habitacion searchById(int id) {
Habitacion h = null;
        String query = "SELECT * FROM Habitaciones WHERE id_habitacion = ?";
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
                h.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error buscar habitacion: " + e.getMessage());
        }
        return h;    }

    @Override
    public boolean delete(int id) {
String query = "UPDATE Habitaciones SET estado = 0 WHERE id_habitacion = ?";
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
