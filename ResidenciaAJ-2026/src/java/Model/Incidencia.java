package Model;

import java.sql.Timestamp;

public class Incidencia {
    private int id_incidencia;
    private String tipo_falla;
    private String descripcion;
    private String foto;
    private Timestamp fecha_reporte;
    private Timestamp fecha_resolucion;
    private String estado;
    private int id_habitacion;
    private int id_usuario;
    
    // Transients
    private String nombre_estudiante;
    private String numero_habitacion;

    public Incidencia() {
    }

    public int getId_incidencia() {
        return id_incidencia;
    }

    public void setId_incidencia(int id_incidencia) {
        this.id_incidencia = id_incidencia;
    }

    public String getTipo_falla() {
        return tipo_falla;
    }

    public void setTipo_falla(String tipo_falla) {
        this.tipo_falla = tipo_falla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Timestamp getFecha_reporte() {
        return fecha_reporte;
    }

    public void setFecha_reporte(Timestamp fecha_reporte) {
        this.fecha_reporte = fecha_reporte;
    }

    public Timestamp getFecha_resolucion() {
        return fecha_resolucion;
    }

    public void setFecha_resolucion(Timestamp fecha_resolucion) {
        this.fecha_resolucion = fecha_resolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getNombre_estudiante() {
        return nombre_estudiante;
    }

    public void setNombre_estudiante(String nombre_estudiante) {
        this.nombre_estudiante = nombre_estudiante;
    }

    public String getNumero_habitacion() {
        return numero_habitacion;
    }

    public void setNumero_habitacion(String numero_habitacion) {
        this.numero_habitacion = numero_habitacion;
    }
}
