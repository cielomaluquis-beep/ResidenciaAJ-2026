package Interface;

import Model.Reserva;
import java.util.List;

public interface IReserva {
    int insertar(Reserva r);
    List<Reserva> listarPorUsuario(int id_usuario);
    List<Reserva> listarTodas();
    boolean actualizarEstado(int id_reserva, String estado);
}
