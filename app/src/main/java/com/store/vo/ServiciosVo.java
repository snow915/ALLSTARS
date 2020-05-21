package com.store.vo;

public class ServiciosVo {
    private String nombreServicio;
    private String precioServicio;

    public ServiciosVo(){}

    public ServiciosVo(String nombreServicio, String precioServicio){
        this.nombreServicio = nombreServicio;
        this.precioServicio = precioServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getPrecioServicio() {
        return precioServicio;
    }

    public void setPrecioServicio(String precioServicio) {
        this.precioServicio = precioServicio;
    }

    @Override
    public String toString() {
        return nombreServicio;
    }
}
