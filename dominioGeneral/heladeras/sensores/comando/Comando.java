package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
public interface Comando {
     void accionar(Heladera heladera, TipoIncidente incidente);
}
