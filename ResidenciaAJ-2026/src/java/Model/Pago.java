package Model;

import java.sql.Date;

public class Pago {
    private int id_pago;
    private double monto;
    private Date fecha;
    private String metodo_pago;
    private String concepto;
    private String img_comprobante;
    private String estado;
    private int id_reserva; // Can be 0 if null in DB
    private int id_contrato; // Can be 0 if null in DB
    private int id_usuario;

    // Transient attributes for view
    private String clienteNombreCompleto;
    private String usuarioNombre;
    private String detalleAsociado; // p.ej. "Contrato #5 - Hab. 101"
    private String numHabitacion;

    public Pago() {
    }

    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getImg_comprobante() {
        return img_comprobante;
    }

    public void setImg_comprobante(String img_comprobante) {
        this.img_comprobante = img_comprobante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public int getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(int id_contrato) {
        this.id_contrato = id_contrato;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getClienteNombreCompleto() {
        return clienteNombreCompleto;
    }

    public void setClienteNombreCompleto(String clienteNombreCompleto) {
        this.clienteNombreCompleto = clienteNombreCompleto;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getDetalleAsociado() {
        return detalleAsociado;
    }

    public void setDetalleAsociado(String detalleAsociado) {
        this.detalleAsociado = detalleAsociado;
    }

    public String getNumHabitacion() {
        return numHabitacion;
    }

    public void setNumHabitacion(String numHabitacion) {
        this.numHabitacion = numHabitacion;
    }
}
