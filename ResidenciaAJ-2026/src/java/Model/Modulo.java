package Model;

public class Modulo {
    private int idModulo;
    private Integer idModuloPadre;
    private String nombre;
    private String ruta;
    private String icono;
    private int orden;

    public Modulo() {
    }

    public Modulo(int idModulo, Integer idModuloPadre, String nombre, String ruta, String icono, int orden) {
        this.idModulo = idModulo;
        this.idModuloPadre = idModuloPadre;
        this.nombre = nombre;
        this.ruta = ruta;
        this.icono = icono;
        this.orden = orden;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public Integer getIdModuloPadre() {
        return idModuloPadre;
    }

    public void setIdModuloPadre(Integer idModuloPadre) {
        this.idModuloPadre = idModuloPadre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
