package Interface;

import Model.Contrato;
import java.util.List;

public interface IContrato {
    public int insert(Contrato contrato);
    public boolean update(Contrato contrato);
    public boolean delete(int id);
    public List<Contrato> listar();
    public Contrato buscar(int id);
    public boolean actualizarEstado(int id_contrato, String estado);
}
