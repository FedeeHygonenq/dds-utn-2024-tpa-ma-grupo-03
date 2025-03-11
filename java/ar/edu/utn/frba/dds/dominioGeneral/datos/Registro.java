package ar.edu.utn.frba.dds.dominioGeneral.datos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class Registro<TIPO> {
    private LocalDateTime fecha;
    private ValorGenerico<TIPO> valor;
    public Registro(ValorGenerico<TIPO> valor) {
        this.fecha = LocalDateTime.now();
        this.valor = valor;
    }

}
