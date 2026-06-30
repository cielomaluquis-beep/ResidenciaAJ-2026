/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Model.Rol;
import java.util.List;

/**
 *
 * @author cielo
 */
public interface IRol {
  List<Rol> lista();
    int insert(Rol r);
    boolean update(Rol r);
    Rol searchById(int id);
    boolean delete(int id);
}
