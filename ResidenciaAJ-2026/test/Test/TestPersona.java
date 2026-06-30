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
        Persona p = new Persona();
        p.setNombre("Cielo");
        p.setApellidos("Tapia");
        p.setDni("98765432");
        p.setTelefono("961267470");
        p.setCorreo("cielo@gmail.com");
        p.setFecha_nacimiento("2005-01-01");

        Usuario u = new Usuario();
        u.setPassword("admin123");

        int result = dao.insertar(p, u);
        if (result > 0) {
            System.out.println("Persona y Usuario creados");
            System.out.println("Usuario: " + p.getCorreo());
            System.out.println("Rol asignado: ESTUDIANTE");
        } else {
            System.out.println("No se pudo realizar el registro");
        }
    }

    public void valid_user() {
        Usuario u = uDao.validate("cielo@gmail.com", "admin123");
        if (u != null && u.getPersona() != null) {
            System.out.println("Bienvenido " + u.getPersona().getNombre() + " " + u.getPersona().getApellidos());
            System.out.println("Rol: " + u.getRol().getNombre());
            System.out.println("Usuario: " + u.getUsername());
            System.out.println("User_id: " + u.getId_Usuario());
            System.out.println("persona_id: " + u.getPersona().getId_persona());
            System.out.println("DNI: " + u.getPersona().getDni());
            System.out.println("Telefono: " + u.getPersona().getTelefono());
            System.out.println("Correo: " + u.getPersona().getCorreo());
        } else {
            System.out.println("Credenciales incorrectas");
        }
        
    }
    
}
