package Interface;

import Model.Alerta;
import java.util.List;

public interface IAlerta {
    public List<Alerta> listarPorUsuario(int id_usuario);
    public boolean insert(Alerta alerta);
    public boolean marcarLeida(int id_alerta);
    public boolean marcarTodasLeidas(int id_usuario);
    public int contarNoLeidas(int id_usuario);
    public boolean existeAlertaReciente(int id_usuario, String tipo_alerta, String mensaje);
}
