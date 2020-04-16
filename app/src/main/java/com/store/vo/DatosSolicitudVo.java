package com.store.vo;

public class DatosSolicitudVo {

    private String fechaInicio;
    private String horaInicio;
    private String tipoEvento;
    private String fechaFin;
    private String horaFin;
    private String tipoPublico;
    private String detalles;
    private String nombreUbicacion;
    private String latitud;
    private String longitud;
    private String nombreSolicitante;
    private String apellidoSolicitante;

    public DatosSolicitudVo(String fechaInicio,
                            String horaInicio,
                            String tipoEvento,
                            String fechaFin,
                            String horaFin,
                            String tipoPublico,
                            String detalles,
                            String nombreUbicacion,
                            String latitud,
                            String longitud,
                            String nombreSolicitante,
                            String apellidoSolicitante) {
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.tipoEvento = tipoEvento;
        this.fechaFin = fechaFin;
        this.horaFin = horaFin;
        this.tipoPublico = tipoPublico;
        this.detalles = detalles;
        this.nombreUbicacion = nombreUbicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreSolicitante = nombreSolicitante;
        this.apellidoSolicitante = apellidoSolicitante;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
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

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getNombreUbicacion() {
        return nombreUbicacion;
    }

    public void setNombreUbicacion(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
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

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getApellidoSolicitante() {
        return apellidoSolicitante;
    }

    public void setApellidoSolicitante(String apellidoSolicitante) {
        this.apellidoSolicitante = apellidoSolicitante;
    }
}
