package Interface;

import Model.Notificacion;
import java.util.List;

public interface INotificacion {
    boolean insertar(Notificacion n);
    List<Notificacion> listarPorUsuario(int id_usuario);
    boolean marcarLeida(int id_notificacion, int id_usuario);
    boolean marcarTodasLeidas(int id_usuario);
    boolean eliminar(int id_notificacion, int id_usuario);
}
