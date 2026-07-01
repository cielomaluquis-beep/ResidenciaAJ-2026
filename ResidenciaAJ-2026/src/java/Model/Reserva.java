package Model;

import java.sql.Date;
import java.sql.Timestamp;

public class Reserva {
    private int id_reserva;
    private Timestamp fecha_reserva;
    private Date fecha_posibleIni;
    private String estado;
    private int id_cliente;
    private int id_habitacion;
    private int id_usuario;

    public Reserva() {
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public Timestamp getFecha_reserva() {
        return fecha_reserva;
    }

    public void setFecha_reserva(Timestamp fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }

    public Date getFecha_posibleIni() {
        return fecha_posibleIni;
    }

    public void setFecha_posibleIni(Date fecha_posibleIni) {
        this.fecha_posibleIni = fecha_posibleIni;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    // Campos transitorios para las vistas
    private String clienteNombre;
    private String habitacionNumero;

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getHabitacionNumero() {
        return habitacionNumero;
    }

    public void setHabitacionNumero(String habitacionNumero) {
        this.habitacionNumero = habitacionNumero;
    }

    private Double pagoMonto;
    private String pagoComprobante;

    public Double getPagoMonto() {
        return pagoMonto;
    }

    public void setPagoMonto(Double pagoMonto) {
        this.pagoMonto = pagoMonto;
    }

    public String getPagoComprobante() {
        return pagoComprobante;
    }

    public void setPagoComprobante(String pagoComprobante) {
        this.pagoComprobante = pagoComprobante;
    }
}
