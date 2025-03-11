package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando.Comando;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Accionador {
    private List<Comando> comandos;

    public Accionador(List<Comando> comandos){
      this.comandos = comandos;
    }
    public void sucedeIncidente(Heladera heladera, TipoIncidente incidente){
        for(Comando comando : comandos){
            comando.accionar(heladera, incidente);
        }

    }
}
