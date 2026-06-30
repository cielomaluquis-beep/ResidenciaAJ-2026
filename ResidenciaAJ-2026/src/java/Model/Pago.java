/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author cielo
 */
public class Pago {
    
    private int id_pago;
    private int id_contrato;
    private double monto;
    private String fecha;
    private String metodo_pago;
    private Contrato contrato;

    public Pago() {
    }

    public Pago(int id_pago, int id_contrato, double monto, String fecha, String metodo_pago, Contrato contrato) {
        this.id_pago = id_pago;
        this.id_contrato = id_contrato;
        this.monto = monto;
        this.fecha = fecha;
        this.metodo_pago = metodo_pago;
        this.contrato = contrato;
    }

    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public int getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(int id_contrato) {
        this.id_contrato = id_contrato;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
    
    
    
}
