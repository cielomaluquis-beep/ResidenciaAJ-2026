/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Model.Pago;
import java.util.List;

/**
 *
 * @author cielo
 */
public interface IPago {
    List<Pago> lista();
    int insert(Pago p);
    boolean update(Pago p);
    Pago searchById(int id);
    boolean delete(int id);
    
}
