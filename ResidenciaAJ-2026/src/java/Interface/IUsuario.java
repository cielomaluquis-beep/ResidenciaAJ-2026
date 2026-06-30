/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interface;

import Model.Usuario;
import java.util.List;

/**
 *
 * @author cielo
 */
public interface IUsuario {
    List<Usuario> lista();
    Usuario validate(String username, String password);
    int insert(Usuario u);
    boolean update(Usuario u);
    Usuario searchById(int id);
    boolean delete(int id);    
}
