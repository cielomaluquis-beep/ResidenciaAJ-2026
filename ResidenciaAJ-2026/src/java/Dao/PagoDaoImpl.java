/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Interface.IPago;
import Model.Pago;
import Util.ConexionSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author cielo
 */
public class PagoDaoImpl implements IPago{
    private Connection cn;

    @Override
    public List<Pago> lista() {
        List<Pago> lista = new ArrayList<>();
        String query = "SELECT p.*, c.id_contrato " +
                       "FROM Pagos p " +
                       "JOIN Contratos c ON p.id_contrato = c.id_contrato";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Pago p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setId_contrato(rs.getInt("id_contrato"));
                p.setMonto(rs.getDouble("monto"));
                p.setFecha(rs.getString("fecha"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error lista pagos: " + e.getMessage());
        }
        return lista;    }

    @Override
    public int insert(Pago p) {
        String query = "INSERT INTO Pagos (id_contrato, monto, fecha, metodo_pago) VALUES (?, ?, ?, ?)";
        int resultado = 0;
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, p.getId_contrato());
            st.setDouble(2, p.getMonto());
            st.setString(3, p.getFecha());
            st.setString(4, p.getMetodo_pago());
            resultado = st.executeUpdate();
            if (resultado > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    p.setId_pago(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar pago: " + e.getMessage());
        }
        return resultado;    }

    @Override
    public boolean update(Pago p) {
        String query = "UPDATE Pagos SET id_contrato=?, monto=?, fecha=?, metodo_pago=? WHERE id_pago=?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, p.getId_contrato());
            st.setDouble(2, p.getMonto());
            st.setString(3, p.getFecha());
            st.setString(4, p.getMetodo_pago());
            st.setInt(5, p.getId_pago());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error update pago: " + e.getMessage());
        }
        return false;    }

    @Override
    public Pago searchById(int id) {
        Pago p = null;
        String query = "SELECT * FROM Pagos WHERE id_pago = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setId_contrato(rs.getInt("id_contrato"));
                p.setMonto(rs.getDouble("monto"));
                p.setFecha(rs.getString("fecha"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
            }
        } catch (SQLException e) {
            System.out.println("Error buscar pago: " + e.getMessage());
        }
        return p;    }

    @Override
    public boolean delete(int id) {
    String query = "DELETE FROM Pagos WHERE id_pago = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error delete pago: " + e.getMessage());
        }
        return false;
    }   
}
    

