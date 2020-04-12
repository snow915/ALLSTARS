package com.store;

public class Solicitud {

    private String ubicacion;
    private String latitud;
    private String longitud;
    private String fechaInicio;
    private String fechaFin;
    private String horaInicio;
    private String horaFin;
    private String tipoPublico;
    private String tipoEvento;
    private String detalles;
    private String userFamoso;

    public String getUserFamoso() {
        return userFamoso;
    }

    public void setUserFamoso(String userFamoso) {
        this.userFamoso = userFamoso;
    }

    public String getNombreFamoso() {
        return nombreFamoso;
    }

    public void setNombreFamoso(String nombreFamoso) {
        this.nombreFamoso = nombreFamoso;
    }

    private String nombreFamoso;

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getTipoPublico() {
        return tipoPublico;
    }

    public void setTipoPublico(String tipoPublico) {
        this.tipoPublico = tipoPublico;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

}
