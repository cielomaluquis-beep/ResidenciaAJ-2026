package Model;

public class Habitacion {
    private int id_habitacion;
    private String numero;
    private String tipo;
    private double precio;
    private int piso;
    private String tipo_bano;
    private String img_habitacion;
    private String estado;

    public Habitacion() {
    }

    public Habitacion(int id_habitacion, String numero, String tipo, double precio, int piso, String tipo_bano, String img_habitacion, String estado) {
        this.id_habitacion = id_habitacion;
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.piso = piso;
        this.tipo_bano = tipo_bano;
        this.img_habitacion = img_habitacion;
        this.estado = estado;
    }

    public int getId_habitacion() {
        return id_habitacion;
    }

    public void setId_habitacion(int id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public String getTipo_bano() {
        return tipo_bano;
    }

    public void setTipo_bano(String tipo_bano) {
        this.tipo_bano = tipo_bano;
    }

    public String getImg_habitacion() {
        return img_habitacion;
    }

    public void setImg_habitacion(String img_habitacion) {
        this.img_habitacion = img_habitacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
