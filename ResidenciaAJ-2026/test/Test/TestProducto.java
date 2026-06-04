/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Test;

import Dao.ProductoDaoImpl;
import Interface.IProducto;
import Model.Productos;
import java.util.List;

/**
 *
 * @author LAB 2
 */
public class TestProducto {

    public static IProducto dao = new ProductoDaoImpl();

    public static void main(String[] args) {
        TestProducto t = new TestProducto();
       // t.insertar();
       // t.listar();
      // t.search();
      // t.update();  
     //  t.updateStock();
    // t.delete();
      
    }

    public static void listar() {
        List<Productos> lista = dao.lista();

        if (lista != null && !lista.isEmpty()) {
            System.out.println("ID\tNombre\tPrecio\tStock");
            for (Productos p : lista) {
                System.out.println(p.getId_producto()
                        + "\t" + p.getNombre() + "\t$"
                        + p.getPrecio() + "\t" + p.getStock());
            }
        } else {
            System.out.println("no hay productos");
        }
    }
    
    public static void insertar(){
        Productos p = new Productos();
        p.setNombre("Mouse Mecanico");
        p.setDescripcion("Retroiluminado");
        p.setPrecio(20.99);
        p.setStock(20);
        p.setImagen("/resouces/img/teclado.jpg");
        boolean result = dao.insert(p);
        if (result) {
            System.out.println("Producto insertado");
        }else{
            System.out.println("Error");
        }  
    }
    
    public static void update(){
        Productos p = new Productos();
        p.setNombre("Arroz Añejo");
        p.setDescripcion("Mas agradable");
        p.setPrecio(4);
        p.setStock(100);
        p.setImagen("/resouces/img/arroz.jpg");
        p.setId_producto(4);
        boolean result = dao.update(p);
        if (result) {
            System.out.println("Producto actualizado");
        }else{
            System.out.println("Error");
        }  
    }
    public static void search(){
        Productos pr = dao.SearchById(4);
        
        if (pr !=null) {
            System.out.println("Producto encontrado");
            System.out.println("ID:"+pr.getId_producto());
            System.out.println("Nombre:"+pr.getNombre());
            System.out.println("Descripcion:"+pr.getDescripcion());
            System.out.println("Precio:"+pr.getPrecio());
            System.out.println("Stock:"+pr.getStock());
            System.out.println("Ruta Img:"+pr.getImagen());
        }else{
            System.out.println("no hay registros");
        }
        
    }
    public static void delete(){
          boolean result = dao.delete(4);
        if (result) {
            System.out.println("Eliminado");
        }else{
            System.out.println("no se elimino");
        }
    }
        public static void updateStock(){
          boolean result = dao.updateStock(4,200);
        if (result) {
            System.out.println("stock actualizado");
        }else{
            System.out.println("no se actualizo");
        }
    }
    
    
    
    
    
}


