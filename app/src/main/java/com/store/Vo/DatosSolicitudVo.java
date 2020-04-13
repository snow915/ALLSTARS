package com.store.Vo;

public class DatosSolicitudVo {

    private String fechaInicio;
    private String horaInicio;
    private String tipoEvento;

    public DatosSolicitudVo(String fechaInicio, String horaInicio, String tipoEvento) {
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.tipoEvento = tipoEvento;
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

}
