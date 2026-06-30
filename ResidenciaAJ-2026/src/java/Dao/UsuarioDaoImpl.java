/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Interface.IUsuario;
import Model.Persona;
import Model.Rol;
import Model.Usuario;
import Util.ConexionSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cielo
 */
public class UsuarioDaoImpl implements IUsuario{
    private Connection cn;

    @Override
    public List<Usuario> lista() {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT u.*, p.nombre, p.apellidos, r.Nombre as rol_nombre " +
                       "FROM Usuarios u " +
                       "JOIN Personas p ON u.id_persona = p.id_persona " +
                       "JOIN Roles r ON u.id_Rol = r.id_Rol " +
                       "WHERE u.estado = 1";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId_Usuario(rs.getInt("id_Usuario"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setId_persona(rs.getInt("id_persona"));
                u.setId_Rol(rs.getInt("id_Rol"));
                u.setEstado(rs.getInt("estado"));

                Persona p = new Persona();
                p.setId_persona(rs.getInt("id_persona"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidos(rs.getString("apellidos"));
                u.setPersona(p);

                Rol r = new Rol();
                r.setId_Rol(rs.getInt("id_Rol"));
                r.setNombre(rs.getString("rol_nombre"));
                u.setRol(r);

                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Error lista usuarios: " + e.getMessage());
        }
        return lista;    }

    @Override
    public Usuario validate(String username, String password) {
        Usuario u = null;
        String query = "SELECT u.*, p.id_persona, p.nombre, p.apellidos, p.correo, p.telefono, p.dni, r.id_Rol, r.Nombre as rol_nombre " +
                       "FROM Usuarios u " +
                       "JOIN Personas p ON u.id_persona = p.id_persona " +
                       "JOIN Roles r ON u.id_Rol = r.id_Rol " +
                       "WHERE u.username = ? AND u.password = ? AND u.estado = 1";
        try {
            cn = ConexionSingleton.getConnection();
            Usuario temp = new Usuario();
            String hashedPassword = temp.HashPassword(password);
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, hashedPassword);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                u = new Usuario();
                u.setId_Usuario(rs.getInt("id_Usuario"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setId_persona(rs.getInt("id_persona"));
                u.setId_Rol(rs.getInt("id_Rol"));
                u.setEstado(rs.getInt("estado"));

                Persona p = new Persona();
                p.setId_persona(rs.getInt("id_persona"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidos(rs.getString("apellidos"));
                p.setCorreo(rs.getString("correo"));
                p.setTelefono(rs.getString("telefono"));
                p.setDni(rs.getString("dni"));
                u.setPersona(p);

                Rol r = new Rol();
                r.setId_Rol(rs.getInt("id_Rol"));
                r.setNombre(rs.getString("rol_nombre"));
                u.setRol(r);
            }
        } catch (SQLException e) {
            System.out.println("Error validar usuario: " + e.getMessage());
        }
        return u;    }

    @Override
    public int insert(Usuario u) {
        String query = "INSERT INTO Usuarios (username, password, id_persona, id_Rol, estado) VALUES (?, ?, ?, ?, ?)";
        int resultado = 0;
        try {
            cn = ConexionSingleton.getConnection();
            String hashedPassword = u.HashPassword(u.getPassword());
            PreparedStatement st = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, u.getUsername());
            st.setString(2, hashedPassword);
            st.setInt(3, u.getId_persona());
            st.setInt(4, u.getId_Rol());
            st.setInt(5, u.getEstado());
            resultado = st.executeUpdate();
            if (resultado > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    u.setId_Usuario(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar usuario: " + e.getMessage());
        }
        return resultado;    }

    @Override
    public boolean update(Usuario u) {
        String query = "UPDATE Usuarios SET username=?, password=?, id_persona=?, id_Rol=?, estado=? WHERE id_Usuario=?";
        try {
            cn = ConexionSingleton.getConnection();
            String hashedPassword = u.HashPassword(u.getPassword());
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, u.getUsername());
            st.setString(2, hashedPassword);
            st.setInt(3, u.getId_persona());
            st.setInt(4, u.getId_Rol());
            st.setInt(5, u.getEstado());
            st.setInt(6, u.getId_Usuario());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error update usuario: " + e.getMessage());
        }
        return false;    }

    @Override
    public Usuario searchById(int id) {
        Usuario u = null;
        String query = "SELECT * FROM Usuarios WHERE id_Usuario = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                u = new Usuario();
                u.setId_Usuario(rs.getInt("id_Usuario"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setId_persona(rs.getInt("id_persona"));
                u.setId_Rol(rs.getInt("id_Rol"));
                u.setEstado(rs.getInt("estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error buscar usuario: " + e.getMessage());
        }
        return u;    }

    @Override
    public boolean delete(int id) {
        String query = "UPDATE Usuarios SET estado = 0 WHERE id_Usuario = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error delete usuario: " + e.getMessage());
        }
        return false;    }
    
}
