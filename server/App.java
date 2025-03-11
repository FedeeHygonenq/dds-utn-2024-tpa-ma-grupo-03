package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.cronJobs.CronInitializer;
import ar.edu.utn.frba.dds.cronJobs.MainComprobarConexionesSensores;
import ar.edu.utn.frba.dds.cronJobs.MainGuardarRegistrosDeSensores;
import ar.edu.utn.frba.dds.cronJobs.MainLimpiarSolicitudesDeAperturaCaducadas;
import ar.edu.utn.frba.dds.cronJobs.MainReportesSemanales;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
  public static void main(String[] args) {
    Server.init();
    CronInitializer.initCronJobs();
  }


}

