package Model;

public class TipoContrato {
    private int id_tipoContrato;
    private String nombre;
    private int dias_duracion;
    private String estado;

    public TipoContrato() {
    }

    public int getId_tipoContrato() {
        return id_tipoContrato;
    }

    public void setId_tipoContrato(int id_tipoContrato) {
        this.id_tipoContrato = id_tipoContrato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDias_duracion() {
        return dias_duracion;
    }

    public void setDias_duracion(int dias_duracion) {
        this.dias_duracion = dias_duracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
