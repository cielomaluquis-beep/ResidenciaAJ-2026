package Dao;

import Interface.IUsuario;
import Model.Rol;
import Model.Usuario;
import Util.ConexionSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoImpl implements IUsuario {
    private Connection cn;

    @Override
    public List<Usuario> lista() {
        return new ArrayList<>();
    }

    @Override
    public Usuario validate(String email, String password) {
        Usuario u = null;
        String query = "SELECT u.*, r.nombre as rol_nombre FROM usuarios u JOIN roles r ON u.id_rol = r.id_rol WHERE u.email = ? AND u.contrasena = ? AND u.habilitado = 1";
        try {
            cn = ConexionSingleton.getConnection();
            Usuario temp = new Usuario();
            String hashedPassword = temp.HashPassword(password);
            
            System.out.println("=== INTENTO DE LOGIN ===");
            System.out.println("Email recibido: " + email);
            System.out.println("Hash generado: " + hashedPassword);
            
            PreparedStatement st = cn.prepareStatement(query);
            st.setString(1, email);
            st.setString(2, hashedPassword);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                u = new Usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setEmail(rs.getString("email"));
                u.setContrasena(rs.getString("contrasena"));
                u.setNombre(rs.getString("nombre"));
                u.setHabilitado(rs.getInt("habilitado"));
                u.setId_rol(rs.getInt("id_rol"));

                Rol r = new Rol();
                r.setId_rol(rs.getInt("id_rol"));
                r.setNombre(rs.getString("rol_nombre"));
                u.setRol(r);
            }
        } catch (SQLException e) {
            System.out.println("=== ERROR SQL EN LOGIN ===");
            System.out.println("Error validar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public int insert(Usuario u) {
        int idGenerado = 0;
        String sql = "INSERT INTO usuarios (email, contrasena, nombre, habilitado, id_rol) VALUES (?, ?, ?, ?, ?)";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(sql, new String[]{"id_usuario"});
            st.setString(1, u.getEmail());
            st.setString(2, u.getContrasena());
            st.setString(3, u.getNombre());
            st.setInt(4, u.getHabilitado());
            st.setInt(5, u.getId_rol());
            
            int filasAfectadas = st.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    // Oracle typically returns rowid or the sequence based on driver, let's just get Int(1)
                    idGenerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error insertar usuario: " + e.getMessage());
        }
        return idGenerado;
    }

    @Override
    public boolean update(Usuario u) {
        return false;
    }

    @Override
    public Usuario searchById(int id) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
