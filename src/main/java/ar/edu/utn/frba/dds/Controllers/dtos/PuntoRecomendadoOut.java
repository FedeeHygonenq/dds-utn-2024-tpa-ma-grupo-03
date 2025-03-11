package ar.edu.utn.frba.dds.Controllers.dtos;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;

public class PuntoRecomendadoOut {
    private Direccion direccion;
    private String descripcion;

    public PuntoRecomendadoOut(Direccion direccion, String descripcion) {
        this.direccion = direccion;
        this.descripcion = descripcion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
