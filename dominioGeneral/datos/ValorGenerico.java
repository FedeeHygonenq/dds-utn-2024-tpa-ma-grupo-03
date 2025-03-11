package ar.edu.utn.frba.dds.dominioGeneral.datos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValorGenerico<TIPO> {
    private final TIPO tipoObjeto;

    public ValorGenerico(TIPO tipoObjeto){
        this.tipoObjeto = tipoObjeto;
    }
}
