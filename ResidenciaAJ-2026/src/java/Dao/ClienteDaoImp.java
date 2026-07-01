package Dao;

import Interface.ICliente;
import Model.Cliente;
import Util.ConexionSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoImp implements ICliente {
    
    private Connection cn;

    @Override
    public List<Cliente> lista() {
        List<Cliente> lista = new ArrayList<>();
        String query = "SELECT * FROM clientes ORDER BY id_cliente DESC";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setAp_paterno(rs.getString("ap_paterno"));
                c.setAp_materno(rs.getString("ap_materno"));
                c.setNro_documento(rs.getString("nro_documento"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setFecha_nac(rs.getDate("fecha_nac"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error lista clientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public int insert(Cliente c) {
        String query = "INSERT INTO clientes (nombre, ap_paterno, ap_materno, nro_documento, telefono, email, fecha_nac) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int idGenerado = 0;
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, c.getNombre());
            st.setString(2, c.getAp_paterno());
            st.setString(3, c.getAp_materno());
            st.setString(4, c.getNro_documento());
            st.setString(5, c.getTelefono());
            st.setString(6, c.getEmail());
            st.setDate(7, c.getFecha_nac());
            
            int resultado = st.executeUpdate();
            if (resultado > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                    c.setId_cliente(idGenerado);
                }
                // Si getGeneratedKeys no funciona bien en Oracle sin ROWID, retornamos 1 como éxito
                if(idGenerado == 0) idGenerado = 1;
            }
        } catch (SQLException e) {
            System.out.println("Error insert cliente: " + e.getMessage());
        }
        return idGenerado;
    }

    @Override
    public boolean update(Cliente c) {
        String query = "UPDATE clientes SET nombre=?, ap_paterno=?, ap_materno=?, nro_documento=?, telefono=?, email=?, fecha_nac=? WHERE id_cliente=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, c.getNombre());
            st.setString(2, c.getAp_paterno());
            st.setString(3, c.getAp_materno());
            st.setString(4, c.getNro_documento());
            st.setString(5, c.getTelefono());
            st.setString(6, c.getEmail());
            st.setDate(7, c.getFecha_nac());
            st.setInt(8, c.getId_cliente());
            
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error update cliente: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM clientes WHERE id_cliente=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error delete cliente: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Cliente searchById(int id) {
        String query = "SELECT * FROM clientes WHERE id_cliente=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setAp_paterno(rs.getString("ap_paterno"));
                c.setAp_materno(rs.getString("ap_materno"));
                c.setNro_documento(rs.getString("nro_documento"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setFecha_nac(rs.getDate("fecha_nac"));
                return c;
            }
        } catch (SQLException e) {
            System.out.println("Error searchById cliente: " + e.getMessage());
        }
        return null;
    }
    @Override
    public Cliente searchByEmail(String email) {
        String query = "SELECT * FROM clientes WHERE email=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setAp_paterno(rs.getString("ap_paterno"));
                c.setAp_materno(rs.getString("ap_materno"));
                c.setNro_documento(rs.getString("nro_documento"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setFecha_nac(rs.getDate("fecha_nac"));
                return c;
            }
        } catch (SQLException e) {
            System.out.println("Error searchByEmail cliente: " + e.getMessage());
        }
        return null;
    }
}
