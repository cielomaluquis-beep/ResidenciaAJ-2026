/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Model.Habitacion;
import java.util.List;

/**
 *
 * @author cielo
 */
public interface IHabitacion {
    List<Habitacion> lista();
    int insert(Habitacion h);
    boolean update(Habitacion h);
    Habitacion searchById(int id);
    boolean delete(int id);
    
}
