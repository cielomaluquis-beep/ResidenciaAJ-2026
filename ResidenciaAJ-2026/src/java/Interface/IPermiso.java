package Interface;

import Model.Permiso;
import java.util.List;

public interface IPermiso {
    List<Permiso> getPermisosByRol(int idRol);
    List<Permiso> getAllModulosConPermisos(int idRol);
    boolean savePermisos(int idRol, List<Permiso> permisos);
}
