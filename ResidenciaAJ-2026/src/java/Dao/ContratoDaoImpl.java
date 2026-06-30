/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Interface.IContrato;
import Model.Contrato;
import Model.Habitacion;
import Model.Persona;
import Util.ConexionSingleton;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author cielo
 */
public class ContratoDaoImpl implements IContrato{
    private Connection cn;

    @Override
    public List<Contrato> lista() {
List<Contrato> lista = new ArrayList<>();
        String query = "SELECT c.*, p.nombre as p_nombre, p.apellidos, h.numero, h.tipo " +
                       "FROM Contratos c " +
                       "JOIN Personas p ON c.id_persona = p.id_persona " +
                       "JOIN Habitaciones h ON c.id_habitacion = h.id_habitacion " +
                       "WHERE c.estado = 1";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Contrato c = new Contrato();
                c.setId_contrato(rs.getInt("id_contrato"));
                c.setId_persona(rs.getInt("id_persona"));
                c.setId_habitacion(rs.getInt("id_habitacion"));
                c.setFecha_inicio(rs.getString("fecha_inicio"));
                c.setFecha_fin(rs.getString("fecha_fin"));
                c.setEstado(rs.getInt("estado"));

                Persona p = new Persona();
                p.setId_persona(rs.getInt("id_persona"));
                p.setNombre(rs.getString("p_nombre"));
                p.setApellidos(rs.getString("apellidos"));
                c.setPersona(p);

                Habitacion h = new Habitacion();
                h.setId_habitacion(rs.getInt("id_habitacion"));
                h.setNumero(rs.getString("numero"));
                h.setTipo(rs.getString("tipo"));
                c.setHabitacion(h);

                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error lista contratos: " + e.getMessage());
        }
        return lista;    }

    @Override
    public int insert(Contrato c) {
String query = "INSERT INTO Contratos (id_persona, id_habitacion, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?)";
        int resultado = 0;
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, c.getId_persona());
            st.setInt(2, c.getId_habitacion());
            st.setString(3, c.getFecha_inicio());
            st.setString(4, c.getFecha_fin());
            st.setInt(5, c.getEstado());
            resultado = st.executeUpdate();
            if (resultado > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    c.setId_contrato(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar contrato: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public boolean update(Contrato c) {
         String query = "UPDATE Contratos SET id_persona=?, id_habitacion=?, fecha_inicio=?, fecha_fin=?, estado=? WHERE id_contrato=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, c.getId_persona());
            st.setInt(2, c.getId_habitacion());
            st.setString(3, c.getFecha_inicio());
            st.setString(4, c.getFecha_fin());
            st.setInt(5, c.getEstado());
            st.setInt(6, c.getId_contrato());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error update contrato: " + e.getMessage());
        }
        return false;    }

    @Override
    public Contrato searchById(int id) {
        Contrato c = null;
        String query = "SELECT * FROM Contratos WHERE id_contrato = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                c = new Contrato();
                c.setId_contrato(rs.getInt("id_contrato"));
                c.setId_persona(rs.getInt("id_persona"));
                c.setId_habitacion(rs.getInt("id_habitacion"));
                c.setFecha_inicio(rs.getString("fecha_inicio"));
                c.setFecha_fin(rs.getString("fecha_fin"));
                c.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error buscar contrato: " + e.getMessage());
        }
        return c;    }

    @Override
    public boolean delete(int id) {
        String query = "UPDATE Contratos SET estado = 0 WHERE id_contrato = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error delete contrato: " + e.getMessage());
        }
        return false;    }
    
}
