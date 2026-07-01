package Interface;

import Model.Cliente;
import java.util.List;

public interface ICliente {
    List<Cliente> lista();
    int insert(Cliente c);
    boolean update(Cliente c);
    boolean delete(int id);
    Cliente searchByEmail(String email);
    Cliente searchById(int id);
}
