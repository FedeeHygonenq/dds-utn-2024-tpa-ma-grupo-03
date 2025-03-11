package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.receptores;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;

public interface Receptor {

  void evaluar(Heladera heladera, String valor);
}
