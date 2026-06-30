/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Dao.HabitacionDaoImp;
import Interface.IHabitacion;
import Model.Habitacion;
import java.util.List;

/**
 *
 * @author cielo
 */
public class TestHabitacion {

    IHabitacion dao = new HabitacionDaoImp();

    public static void main(String[] args) {
        TestHabitacion t = new TestHabitacion();
        t.testListar();
        t.testInsertar();
    }

    public void testListar() {
        System.out.println("LISTAR HABITACIONES");
        List<Habitacion> lista = dao.lista();
        for (Habitacion h : lista) {
            System.out.println("ID: " + h.getId_habitacion() + " | Numero: " + h.getNumero() + 
                             " | Tipo: " + h.getTipo() + " | Precio: " + h.getPrecio());
        }
    }

    public void testInsertar() {
        System.out.println("INSERTAR HABITACION");
        Habitacion h = new Habitacion();
        h.setNumero("101");
        h.setTipo("Simple");
        h.setPrecio(350.00);
        h.setEstado(1);
        int result = dao.insert(h);
        if (result > 0) {
            System.out.println("Habitacion registrada con ID: " + h.getId_habitacion());
        } else {
            System.out.println("Error al registrar habitacion");
        }
    }
    
    }
    

