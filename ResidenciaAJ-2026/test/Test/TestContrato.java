/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Dao.ContratoDaoImp;
import Interface.IContrato;
import Model.Contrato;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author cielo
 */
public class TestContrato {
    IContrato dao = new ContratoDaoImp();
    
    public static void main(String[] args) {
        TestContrato t = new TestContrato();
        t.testInsertar();
        t.testListar();
    }

    public void testListar() {
        System.out.println("=== LISTAR CONTRATOS ===");
        List<Contrato> lista = dao.listar();
        for (Contrato c : lista) {
            System.out.println("ID: " + c.getId_contrato() + 
                             " | Cliente: " + c.getClienteNombreCompleto() +
                             " | Habitacion: " + c.getHabitacionNumero() +
                             " | Inicio: " + c.getFecha_inicio() + " | Fin: " + c.getFecha_fin());
        }
    }

    public void testInsertar() {
        System.out.println("=== INSERTAR CONTRATO ===");
        Contrato c = new Contrato();
        c.setId_cliente(1);
        c.setId_habitacion(1);
        c.setId_tipoContrato(1);
        c.setId_usuario(1); // El admin o empleado que registra
        c.setFecha_inicio(Date.valueOf("2026-06-01"));
        c.setFecha_fin(Date.valueOf("2026-12-31"));
        c.setEstado("1");
        
        int result = dao.insert(c);
        if (result > 0) {
            System.out.println("Contrato registrado exitosamente con el ID!");
        } else {
            System.out.println("Error al registrar contrato. (Verifica que los IDs existan en BD)");
        }
    }
}
    

