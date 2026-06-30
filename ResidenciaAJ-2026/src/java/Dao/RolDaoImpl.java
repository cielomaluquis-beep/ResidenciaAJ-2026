/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Interface.IRol;
import Model.Rol;
import Util.ConexionSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cielo
 */
public class RolDaoImpl implements IRol{
    private Connection cn;

    @Override
    public List<Rol> lista() {
        List<Rol> lista = new ArrayList<>();
        String query = "SELECT * FROM Roles WHERE Estado = 1";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Rol r = new Rol();
                r.setId_Rol(rs.getInt("id_Rol"));
                r.setNombre(rs.getString("Nombre"));
                r.setDescripcion(rs.getString("Descripcion"));
                r.setEstado(rs.getInt("Estado"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error lista roles: " + e.getMessage());
        }
        return lista;
    }   


    @Override
    public int insert(Rol r) {
        String query = "INSERT INTO Roles (Nombre, Descripcion, Estado) VALUES (?, ?, ?)";
        int resultado = 0;
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, r.getNombre());
            st.setString(2, r.getDescripcion());
            st.setInt(3, r.getEstado());
            resultado = st.executeUpdate();
            if (resultado > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    r.setId_Rol(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar rol: " + e.getMessage());
        }
        return resultado;    }

    @Override
    public boolean update(Rol r) {
        String query = "UPDATE Roles SET Nombre=?, Descripcion=?, Estado=? WHERE id_Rol=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, r.getNombre());
            st.setString(2, r.getDescripcion());
            st.setInt(3, r.getEstado());
            st.setInt(4, r.getId_Rol());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error update rol: " + e.getMessage());
        }
        return false;    }

    @Override
    public Rol searchById(int id) {
        Rol r = null;
        String query = "SELECT * FROM Roles WHERE id_Rol = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                r = new Rol();
                r.setId_Rol(rs.getInt("id_Rol"));
                r.setNombre(rs.getString("Nombre"));
                r.setDescripcion(rs.getString("Descripcion"));
                r.setEstado(rs.getInt("Estado"));
            }
        } catch (SQLException e) {
            System.out.println("Error buscar rol: " + e.getMessage());
        }
        return r;    }

    @Override
    public boolean delete(int id) {
String query = "UPDATE Roles SET Estado = 0 WHERE id_Rol = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error delete rol: " + e.getMessage());
        }
        return false;
    }

}
