/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Dao.PersonaDaoImpl;
import Dao.UsuarioDaoImpl;
import Interface.IPersona;
import Interface.IUsuario;
import Model.Persona;
import Model.Usuario;

/**
 *
 * @author cielo
 */
public class TestPersona {

    IPersona dao = new PersonaDaoImpl();
    IUsuario uDao = new UsuarioDaoImpl();

    public static void main(String[] args) {
        TestPersona t = new TestPersona();
        // t.insert();
        t.valid_user();
    }

    public void insert() {
        System.out.println("Método de prueba insert() deshabilitado temporalmente debido a la refactorización.");
    }

    public void valid_user() {
        Usuario u = uDao.validate("admin@residencia.com", "admin123");
        if (u != null) {
            System.out.println("Bienvenido " + u.getNombre());
            System.out.println("Rol: " + u.getRol().getNombre());
            System.out.println("Email: " + u.getEmail());
            System.out.println("User_id: " + u.getId_usuario());
        } else {
            System.out.println("Credenciales incorrectas");
        }
    }
    
}
