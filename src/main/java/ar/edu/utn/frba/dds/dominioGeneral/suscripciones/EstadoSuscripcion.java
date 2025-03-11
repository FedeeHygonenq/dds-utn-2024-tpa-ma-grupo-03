package ar.edu.utn.frba.dds.dominioGeneral.suscripciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoSuscripcion {

    public Boolean estado;
    public String mensaje;

    public EstadoSuscripcion(boolean estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }
}
