package com.example.inventariowall;

public class Instalacion {
    private String id;
    private String cliente;
    private String direccion;
    private String fecha;
    private String tecnico;
    private String estado;
    private String observaciones;

    // Constructor, getters y setters

    public Instalacion() {
        // Constructor vacío requerido por Firebase
    }

    public Instalacion(String cliente, String direccion, String fecha,
                       String tecnico, String estado, String observaciones) {
        this.cliente = cliente;
        this.direccion = direccion;
        this.fecha = fecha;
        this.tecnico = tecnico;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
