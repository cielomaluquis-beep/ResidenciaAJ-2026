/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Dao.pedidoDaoImpl;
import Interface.IPedido;
import Model.Carrito;
import Model.EstadoPedido;
import Model.Pedidos;
import Model.Persona;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LAB 2
 */
public class TestPedido {
    IPedido dao = new pedidoDaoImpl();
    public static void main(String[] args) {  
       TestPedido t = new TestPedido();
       t.TestPedido();
    }
    
    public void TestPedido(){
        Persona p = new Persona();
        p.setId_persona(1);
        
        List<Carrito> listaCarrito = new ArrayList<>();
        
        Carrito item1 = new Carrito();
        item1.setIdProducto(1);
        item1.setPrecioCompra(50);
        item1.setCantidad(2);
        item1.setSubTotal(100);
        listaCarrito.add(item1);
        
        Carrito item2 = new Carrito();
        item2.setIdProducto(2);
        item2.setPrecioCompra(40);
        item2.setCantidad(3);
        item2.setSubTotal(120);
        listaCarrito.add(item2);
        
        double total = 100+120;
        
        Pedidos nuevoPedido = new Pedidos();
        nuevoPedido.setPersona(p);
        nuevoPedido.setTotal(total);
        nuevoPedido.setEstadoPedido(EstadoPedido.ENVIADO);
        nuevoPedido.setDetallePedido(listaCarrito);
        
        System.out.println("Enviando....");
        int result = dao.generarPedido(nuevoPedido);
        if (result>0) {
            System.out.println("Pedido registrado");
            System.out.println("Total:"+total);
        }else{
            System.out.println("Error al generar el pedido");
        }
        
        
    }
    
}
