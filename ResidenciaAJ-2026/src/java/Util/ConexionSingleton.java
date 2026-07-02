/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;
import java.sql.*;
/**
 *
 * @author LAB 2
 */
public class ConexionSingleton {
    //creado una variable estatica 
    public static Connection connection;

    //metodo getConnection
    public static Connection getConnection() {
        try {
            if (connection == null) {
                Runtime.getRuntime().addShutdownHook(new getClose());
                Class.forName("oracle.jdbc.OracleDriver");
                // Asegúrate de cambiar 'xe', 'usuario' y 'contraseña' por los de tu base de datos Oracle
                connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "RESIDENCIA", "admin123");
                System.out.println("Entro al if");
            }
            return connection;

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Conexion fallida", e);

        }
    }

    static class getClose extends Thread {

        @Override
        public void run() {
            try {
                Connection conn = ConexionSingleton.getConnection();
                conn.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }

    }
}
