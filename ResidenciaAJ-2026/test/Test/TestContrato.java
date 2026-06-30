/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Dao.ContratoDaoImpl;
import Interface.IContrato;
import Model.Contrato;
import java.util.List;

/**
 *
 * @author cielo
 */
public class TestContrato {
    IContrato dao = new ContratoDaoImpl();
   public static void main(String[] args) {
        TestContrato t = new TestContrato();
//        t.testListar();
        t.testInsertar();
    }

    public void testListar() {
        System.out.println("=== LISTAR CONTRATOS ===");
        List<Contrato> lista = dao.lista();
        for (Contrato c : lista) {
            System.out.println("ID: " + c.getId_contrato() + 
                             " | Persona: " + c.getPersona().getNombre() + " " + c.getPersona().getApellidos() +
                             " | Habitacion: " + c.getHabitacion().getNumero() +
                             " | Inicio: " + c.getFecha_inicio() + " | Fin: " + c.getFecha_fin());
        }
    }

    public void testInsertar() {
        System.out.println("=== INSERTAR CONTRATO ===");
        Contrato c = new Contrato();
        c.setId_persona(1);
        c.setId_habitacion(1);
        c.setFecha_inicio("2026-06-01");
        c.setFecha_fin("2026-12-31");
        c.setEstado(1);
        int result = dao.insert(c);
        if (result > 0) {
            System.out.println("Contrato registrado con ID: " + c.getId_contrato());
        } else {
            System.out.println("Error al registrar contrato");
        }
    }
}
    

