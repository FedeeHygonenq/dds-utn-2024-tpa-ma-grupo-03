package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;

public class CambiarEstadoHeladera implements Comando {
  @Override
  public void accionar(Heladera heladera, TipoIncidente incidente) {
    switch (incidente) {
      case TEMPERATURA:
        heladera.setEstado(EstadoHeladera.FALLA_DE_TEMPERATURA);
        break;
      case FALLA_TECNICA:
        heladera.setEstado(EstadoHeladera.FALLA_TECNICA);
        break;
      case FRAUDE:
        heladera.setEstado(EstadoHeladera.FALLA_DE_FRAUDE);
        break;
      case FALLA_CONEXION:
        heladera.setEstado(EstadoHeladera.FALLA_DE_CONEXION);
        break;
    }
    ServiceLocator.heladeraService().agregarHeladera(heladera);
  }
}
