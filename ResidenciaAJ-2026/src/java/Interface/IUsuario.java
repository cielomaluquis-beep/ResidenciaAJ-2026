package Interface;

import Model.Usuario;
import java.util.List;

public interface IUsuario {
    List<Usuario> lista();
    Usuario validate(String email, String password);
    int insert(Usuario u);
    boolean update(Usuario u);
    Usuario searchById(int id);
    boolean delete(int id);    
}
