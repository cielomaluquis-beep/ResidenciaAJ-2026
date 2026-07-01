package Dao;

import Interface.IPermiso;
import Model.Modulo;
import Model.Permiso;
import Util.ConexionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermisoDaoImpl implements IPermiso {
    private Connection cn;

    @Override
    public List<Permiso> getPermisosByRol(int idRol) {
        List<Permiso> lista = new ArrayList<>();
        String query = "SELECT p.*, m.id_modulo_padre, m.nombre as modulo_nombre, m.ruta, m.icono, m.orden " +
                       "FROM permisos_rol_modulo p " +
                       "JOIN modulos m ON p.id_modulo = m.id_modulo " +
                       "WHERE p.id_rol = ?";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, idRol);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Permiso p = new Permiso();
                p.setIdPermiso(rs.getInt("id_permiso"));
                p.setVerModulo(rs.getBoolean("ver_modulo"));
                p.setPView(rs.getBoolean("p_view"));
                p.setPCreate(rs.getBoolean("p_create"));
                p.setPEdit(rs.getBoolean("p_edit"));
                p.setPDelete(rs.getBoolean("p_delete"));
                p.setIdRol(rs.getInt("id_rol"));
                p.setIdModulo(rs.getInt("id_modulo"));

                Modulo m = new Modulo();
                m.setIdModulo(rs.getInt("id_modulo"));
                int padre = rs.getInt("id_modulo_padre");
                m.setIdModuloPadre(rs.wasNull() ? null : padre);
                m.setNombre(rs.getString("modulo_nombre"));
                m.setRuta(rs.getString("ruta"));
                m.setIcono(rs.getString("icono"));
                m.setOrden(rs.getInt("orden"));

                p.setModulo(m);
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error obtener permisos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Permiso> getAllModulosConPermisos(int idRol) {
        List<Permiso> lista = new ArrayList<>();
        String query = "SELECT m.id_modulo, m.nombre as modulo_nombre, m.ruta, m.icono, m.orden, " +
                       "p.id_permiso, p.ver_modulo, p.p_view, p.p_create, p.p_edit, p.p_delete " +
                       "FROM modulos m " +
                       "LEFT JOIN permisos_rol_modulo p ON m.id_modulo = p.id_modulo AND p.id_rol = ? " +
                       "ORDER BY m.orden";
        try {
            cn = ConexionSingleton.getConnection();
            PreparedStatement st = cn.prepareStatement(query);
            st.setInt(1, idRol);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Permiso p = new Permiso();
                p.setIdModulo(rs.getInt("id_modulo"));
                p.setIdRol(idRol);
                
                int idPermiso = rs.getInt("id_permiso");
                if (!rs.wasNull()) {
                    p.setIdPermiso(idPermiso);
                    p.setVerModulo(rs.getBoolean("ver_modulo"));
                    p.setPView(rs.getBoolean("p_view"));
                    p.setPCreate(rs.getBoolean("p_create"));
                    p.setPEdit(rs.getBoolean("p_edit"));
                    p.setPDelete(rs.getBoolean("p_delete"));
                } else {
                    p.setIdPermiso(0);
                    p.setVerModulo(false);
                    p.setPView(false);
                    p.setPCreate(false);
                    p.setPEdit(false);
                    p.setPDelete(false);
                }

                Modulo m = new Modulo();
                m.setIdModulo(rs.getInt("id_modulo"));
                m.setNombre(rs.getString("modulo_nombre"));
                m.setRuta(rs.getString("ruta"));
                m.setIcono(rs.getString("icono"));
                m.setOrden(rs.getInt("orden"));

                p.setModulo(m);
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error getAllModulosConPermisos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean savePermisos(int idRol, List<Permiso> permisos) {
        String deleteQuery = "DELETE FROM permisos_rol_modulo WHERE id_rol = ?";
        String insertQuery = "INSERT INTO permisos_rol_modulo (ver_modulo, p_view, p_create, p_edit, p_delete, id_rol, id_modulo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean exito = false;
        try {
            cn = ConexionSingleton.getConnection();
            cn.setAutoCommit(false); 
            
            try (PreparedStatement pstDelete = cn.prepareStatement(deleteQuery)) {
                pstDelete.setInt(1, idRol);
                pstDelete.executeUpdate();
            }

            try (PreparedStatement pstInsert = cn.prepareStatement(insertQuery)) {
                for (Permiso p : permisos) {
                    if (p.isVerModulo() || p.isPView() || p.isPCreate() || p.isPEdit() || p.isPDelete()) {
                        pstInsert.setInt(1, p.isVerModulo() ? 1 : 0);
                        pstInsert.setInt(2, p.isPView() ? 1 : 0);
                        pstInsert.setInt(3, p.isPCreate() ? 1 : 0);
                        pstInsert.setInt(4, p.isPEdit() ? 1 : 0);
                        pstInsert.setInt(5, p.isPDelete() ? 1 : 0);
                        pstInsert.setInt(6, idRol);
                        pstInsert.setInt(7, p.getIdModulo());
                        pstInsert.addBatch();
                    }
                }
                pstInsert.executeBatch();
            }

            cn.commit();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error savePermisos: " + e.getMessage());
            try {
                if (cn != null) cn.rollback();
            } catch (SQLException ex) {
            }
        } finally {
            try {
                if (cn != null) cn.setAutoCommit(true);
            } catch (SQLException ex) {}
        }
        return exito;
    }
}
