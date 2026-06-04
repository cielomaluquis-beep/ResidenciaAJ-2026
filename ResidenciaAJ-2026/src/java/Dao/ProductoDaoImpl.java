/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Interface.IProducto;
import Model.Productos;
import Util.ConexionSingleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LAB 2
 */
public class ProductoDaoImpl implements IProducto {

    private Connection cn;

    @Override
    public List<Productos> lista() {
        List<Productos> lista = null;
        Productos pr;
        PreparedStatement st;
        ResultSet rs;
        String query = null;
        try {
            query = " SELECT id_producto,nombre,descripcion,precio,stock,imagen FROM productos;";
            lista = new ArrayList<>();

            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                pr = new Productos();
                pr.setId_producto(rs.getInt("id_producto"));
                pr.setNombre(rs.getString("nombre"));
                pr.setDescripcion(rs.getString("descripcion"));
                pr.setPrecio(rs.getDouble("precio"));
                pr.setStock(rs.getInt("stock"));
                pr.setImagen(rs.getString("imagen"));
                lista.add(pr);
            }

        } catch (Exception e) {
            System.out.println("Error al listar:" + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            System.out.println("No se pudo listar el producto");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }

            }
        }
        return lista;
    }

    @Override
    public boolean insert(Productos p) {
        boolean flag = false;
        PreparedStatement st;
        String query;
        try {
            query = "INSERT INTO productos "
                    + "( nombre,descripcion,precio,stock,imagen)"
                    + " VALUES(?,?,?,?,?)";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setString(1, p.getNombre());
            st.setString(2, p.getDescripcion());
            st.setDouble(3, p.getPrecio());
            st.setInt(4, p.getStock());
            st.setString(5, p.getImagen());
            st.executeUpdate();
            flag = true;

        } catch (Exception e) {
            System.out.println("Error al insertar:" + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            flag = false;
            System.out.println("No se pudo insertar el producto");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }

            }
        }

        return flag;

    }

    @Override
    public boolean update(Productos p) {
        boolean flag = false;
        PreparedStatement st;
        String query = null;
        try {
            query = " UPDATE productos SET nombre=?, "
                    + " descripcion=?, precio=?,stock=?, imagen=?"
                    + " WHERE id_producto=?";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setString(1, p.getNombre());
            st.setString(2, p.getDescripcion());
            st.setDouble(3, p.getPrecio());
            st.setInt(4, p.getStock());
            st.setString(5, p.getImagen());
            st.setInt(6, p.getId_producto());
            st.executeUpdate();
            flag = true;

        } catch (Exception e) {
            System.out.println("Error al actualizar:" + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            flag = false;
            System.out.println("No se pudo actualizar el producto");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }

            }
        }

        return flag;
    }

    @Override
    public Productos SearchById(int id) {
        Productos pr = null;
        PreparedStatement st;
        ResultSet rs;
        String query = null;
        try {
            query = " SELECT * FROM productos WHERE id_producto=?;";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                pr = new Productos();
                pr.setId_producto(rs.getInt("id_producto"));
                pr.setNombre(rs.getString("nombre"));
                pr.setDescripcion(rs.getString("descripcion"));
                pr.setPrecio(rs.getDouble("precio"));
                pr.setStock(rs.getInt("stock"));
                pr.setImagen(rs.getString("imagen"));
            }

        } catch (Exception e) {
            System.out.println("Error al buscar por ID:" + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            System.out.println("No se pudo buscar por ID");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }

            }
        }
        return pr;

    }

    @Override
    public boolean delete(int id) {
        boolean flag = false;
        PreparedStatement st;
        String query = null;
        try {
            query = " DELETE FROM productos WHERE id_producto=?";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();
            flag = true;

        } catch (Exception e) {
            System.out.println("Error al eliminar:" + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            flag = false;
            System.out.println("No se pudo eliminar el producto");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }

            }
        }

        return flag;
    }

    @Override
    public boolean updateStock(int id, int stock) {
        boolean flag = false;
        PreparedStatement st;
        String query = null;
        try {
            query = " UPDATE productos SET stock=? WHERE id_producto=?";
            cn = ConexionSingleton.getConnection();
            st = cn.prepareStatement(query);
            st.setInt(1, stock);
            st.setInt(2, id);
            st.executeUpdate();
            flag = true;

        } catch (Exception e) {
            System.out.println("Error al actualizar:" + e.getMessage());
            try {
                cn.rollback();
            } catch (Exception ex) {
            }
            flag = false;
            System.out.println("No se pudo actualizar el producto");
        } finally {
            if (cn != null) {
                try {
                } catch (Exception ex) {
                }

            }
        }

        return flag;
    }

}
