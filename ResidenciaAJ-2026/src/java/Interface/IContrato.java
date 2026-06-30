/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Model.Contrato;
import java.util.List;

/**
 *
 * @author cielo
 */
public interface IContrato {
    List<Contrato> lista();
    int insert(Contrato c);
    boolean update(Contrato c);
    Contrato searchById(int id);
    boolean delete(int id);
    
}
