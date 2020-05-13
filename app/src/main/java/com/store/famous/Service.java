package com.store.famous;

public class Service {

    private String detalles;
    private String nombre_servicio;
    private int precio;
    private int tiempo_contratacion_maximo;

    public void setNombre(String nombre) {
        this.nombre_servicio = nombre;
    }
    public String getNombre() {
        return this.nombre_servicio;
    }
    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
    public String getDetalles() {
        return this.detalles;
    }
    public void setPrecio(int precio) {
        this.precio = precio;
    }
    public int getPrecio() {
        return this.precio;
    }
    public void setTiempoMaximo(int horas) {
        this.tiempo_contratacion_maximo = horas;
    }
    public int getTiempoMaximo() {
        return this.tiempo_contratacion_maximo;
    }
}
