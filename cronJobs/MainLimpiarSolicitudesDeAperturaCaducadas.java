package ar.edu.utn.frba.dds.cronJobs;

import ar.edu.utn.frba.dds.Controllers.HeladeraController;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import java.util.List;

public class MainLimpiarSolicitudesDeAperturaCaducadas implements Runnable{

  @Override
  public void run(){
    ServiceLocator.heladeraController().limpiarSolicitudesCaducadas();

  }
}
