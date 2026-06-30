/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Interface.IPersona;
import Model.Persona;
import Model.Usuario;
import Util.ConexionSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author cielo
 */
public class PersonaDaoImpl implements IPersona{
    private Connection cn;

    @Override
    public List<Persona> lista() {
        List<Persona> lista = new ArrayList<>();
        String query = "SELECT * FROM Personas WHERE estado = 1";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Persona p = new Persona();
                p.setId_persona(rs.getInt("id_persona"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidos(rs.getString("apellidos"));
                p.setDni(rs.getString("dni"));
                p.setTelefono(rs.getString("telefono"));
                p.setCorreo(rs.getString("correo"));
                p.setFecha_nacimiento(rs.getString("fecha_nacimiento"));
                p.setEstado(rs.getInt("estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error lista personas: " + e.getMessage());
        }
        return lista;    }

    @Override
    public int insertar(Persona p, Usuario u) {
        PreparedStatement st;
        String query = null;
        ResultSet rs;
        int id_persona = 0;
        int r = 0;
        try {
            query = "INSERT INTO Persona(nombre, apellidos, dni, telefono, correo, fecha_nacimiento, estado)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, p.getNombre());
            st.setString(2, p.getApellidos());
            st.setString(3, p.getDni());
            st.setString(4, p.getTelefono());
            st.setString(5, p.getCorreo());
            st.setString(6, p.getFecha_nacimiento());
            st.setInt(7, 1);
            r = st.executeUpdate();

            if (r != 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    id_persona = rs.getInt(1);
                    System.out.println("id_persona recuperado: " + id_persona);
                }
                if (id_persona > 0) {
                    // Asignar rol por defecto ESTUDIANTE (id=3)
                    u.setId_Rol(3);
                    String hashedPassword = u.HashPassword(u.getPassword());
                    query = "INSERT INTO Usuarios(username, password, id_persona, id_Rol, estado)"
                            + " VALUES (?, ?, ?, ?, ?)";
                    st = cn.prepareStatement(query);
                    st.setString(1, p.getCorreo()); // username = correo
                    st.setString(2, hashedPassword);
                    st.setInt(3, id_persona);
                    st.setInt(4, u.getId_Rol());
                    st.setInt(5, 1);
                    r = st.executeUpdate();
                } else {
                    System.out.println("Error al agregar persona");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al agregar: " + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
                System.out.println("Error de rollback: " + ex.getMessage());
            }
        }
        return r;    }

    @Override
    public boolean update(Persona p) {
        String query = "UPDATE Personas SET nombre=?, apellidos=?, dni=?, telefono=?, correo=?, fecha_nacimiento=? WHERE id_persona=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, p.getNombre());
            st.setString(2, p.getApellidos());
            st.setString(3, p.getDni());
            st.setString(4, p.getTelefono());
            st.setString(5, p.getCorreo());
            st.setString(6, p.getFecha_nacimiento());
            st.setInt(7, p.getId_persona());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error update persona: " + e.getMessage());
        }
        return false;    }

    @Override
    public Persona searchById(int id) {
        Persona p = null;
        String query = "SELECT * FROM Personas WHERE id_persona = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                p = new Persona();
                p.setId_persona(rs.getInt("id_persona"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidos(rs.getString("apellidos"));
                p.setDni(rs.getString("dni"));
                p.setTelefono(rs.getString("telefono"));
                p.setCorreo(rs.getString("correo"));
                p.setFecha_nacimiento(rs.getString("fecha_nacimiento"));
                p.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error buscar persona: " + e.getMessage());
        }
        return p;    }

    @Override
    public boolean delete(int id) {
String query = "UPDATE Personas SET estado = 0 WHERE id_persona = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error delete persona: " + e.getMessage());
        }
        return false;
    }
    
}
