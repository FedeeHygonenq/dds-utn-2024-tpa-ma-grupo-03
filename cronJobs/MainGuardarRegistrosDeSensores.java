package ar.edu.utn.frba.dds.cronJobs;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorTemperatura;
import ar.edu.utn.frba.dds.services.imp.SensorService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import java.util.List;

public class MainGuardarRegistrosDeSensores implements Runnable {

  @Override
  public void run() {
    SensorService sensorService =  ServiceLocator.sensorService();
    List<SensorTemperatura> sensoresTemperatura = sensorService.traerSensoresTemperatura();
    List<SensorMovimiento> sensoresMovimiento = sensorService.traerSensoresMovimiento();
    for(SensorTemperatura sensor : sensoresTemperatura) {
      sensorService.guardarRegistroSensorTemperatura(sensor);
    }
    for(SensorMovimiento sensor : sensoresMovimiento) {
      sensorService.guardarRegistroSensorMovimiento(sensor);
    }
  }
}
