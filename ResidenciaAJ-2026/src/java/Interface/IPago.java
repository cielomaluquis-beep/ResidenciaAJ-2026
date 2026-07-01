package Interface;

import Model.Pago;
import java.util.List;

public interface IPago {
    public List<Pago> listar();
    public Pago buscar(int id);
    public boolean insert(Pago pago);
    public boolean anular(int id);
}
