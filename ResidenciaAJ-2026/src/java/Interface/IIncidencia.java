package Interface;

import Model.Incidencia;
import java.util.List;

public interface IIncidencia {
    public List<Incidencia> listar();
    public List<Incidencia> listarPorUsuario(int id_usuario);
    public boolean insert(Incidencia incidencia);
    public boolean actualizarEstado(int id_incidencia, String estado);
}
