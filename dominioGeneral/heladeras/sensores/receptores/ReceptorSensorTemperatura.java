package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.receptores;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.Accionador;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando.CambiarEstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando.Comando;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando.LanzarAlerta;
import java.util.ArrayList;
import java.util.List;


public class ReceptorSensorTemperatura implements Receptor {
    private Accionador accionador;

    public ReceptorSensorTemperatura() {
        List<Comando> comandos = new ArrayList<>();
        comandos.add(new CambiarEstadoHeladera());
        comandos.add(new LanzarAlerta());
        this.accionador = new Accionador(comandos);
    }

    @Override
    public void evaluar(Heladera heladera, String valor){
        if(debeLanzarAlerta(heladera, valor) ){
            accionador.sucedeIncidente(heladera,TipoIncidente.TEMPERATURA);
        }

    }
    private Boolean debeLanzarAlerta(Heladera heladera, String valor){
        return (heladera.getModelo().getTemperaturaMaxima() < Integer.parseInt(valor) ||
            heladera.getModelo().getTemperaturaMinima() > Integer.parseInt(valor));

    }
}
