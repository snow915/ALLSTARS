package com.store.vo;

public class DatosSolicitudVo {

    private String fechaInicio;
    private String horaInicio;
    private String tipoEvento;
    private String fechaFin;
    private String horaFin;
    private String tipoPublico;
    private String detalles;
    private String ubicacion;
    private String latitud;
    private String longitud;
    private String userName;
    private String userLastname;
    private String nombreServicio;
    private String precioServicio;
    private String solicitudID;
    private String nombreFamoso;
    private String userFamoso;
    private String userID;

    public DatosSolicitudVo(){}

    public DatosSolicitudVo(String idSolicitud,
                            String fechaInicio,
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
                            String apellidoSolicitante,
                            String nombreServicio,
                            String precioServicio,
                            String nombreFamoso,
                            String userFamoso,
                            String userID) {
        this.solicitudID = idSolicitud;
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.tipoEvento = tipoEvento;
        this.fechaFin = fechaFin;
        this.horaFin = horaFin;
        this.tipoPublico = tipoPublico;
        this.detalles = detalles;
        this.ubicacion = nombreUbicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.userName = nombreSolicitante;
        this.userLastname = apellidoSolicitante;
        this.nombreServicio = nombreServicio;
        this.precioServicio = precioServicio;
        this.nombreFamoso = nombreFamoso;
        this.userFamoso = userFamoso;
        this.userID = userID;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
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

    public String getSolicitudID() {
        return solicitudID;
    }

    public void setSolicitudID(String solicitudID) {
        this.solicitudID = solicitudID;
    }

    public String getNombreFamoso() {
        return nombreFamoso;
    }

    public void setNombreFamoso(String nombreFamoso) {
        this.nombreFamoso = nombreFamoso;
    }

    public String getUserFamoso() {
        return userFamoso;
    }

    public void setUserFamoso(String userFamoso) {
        this.userFamoso = userFamoso;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
