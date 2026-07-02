package Model;

import java.sql.Date;

public class Contrato {
    private int id_contrato;
    private Date fecha_inicio;
    private Date fecha_fin;
    private String estado;
    private int id_tipoContrato;
    private int id_cliente;
    private int id_habitacion;
    private int id_usuario;

    // Campos extra para la vista (transient)
    private String tipoContratoNombre;
    private String clienteNombreCompleto;
    private String habitacionNumero;
    private String usuarioNombre;

    public Contrato() {
    }

    public int getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(int id_contrato) {
        this.id_contrato = id_contrato;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_tipoContrato() {
        return id_tipoContrato;
    }

    public void setId_tipoContrato(int id_tipoContrato) {
        this.id_tipoContrato = id_tipoContrato;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_habitacion() {
        return id_habitacion;
    }

    public void setId_habitacion(int id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    // Getters y Setters Extras
    public String getTipoContratoNombre() {
        return tipoContratoNombre;
    }

    public void setTipoContratoNombre(String tipoContratoNombre) {
        this.tipoContratoNombre = tipoContratoNombre;
    }

    public String getClienteNombreCompleto() {
        return clienteNombreCompleto;
    }

    public void setClienteNombreCompleto(String clienteNombreCompleto) {
        this.clienteNombreCompleto = clienteNombreCompleto;
    }

    public String getHabitacionNumero() {
        return habitacionNumero;
    }

    public void setHabitacionNumero(String habitacionNumero) {
        this.habitacionNumero = habitacionNumero;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
}
