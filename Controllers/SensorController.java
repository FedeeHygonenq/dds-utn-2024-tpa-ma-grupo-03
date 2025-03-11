package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroTemperatura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorTemperatura;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import ar.edu.utn.frba.dds.services.ISensorService;

import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import io.javalin.http.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SensorController {

  private ISensorService sensorService;

  public SensorController(ISensorService sensorService) {
    this.sensorService = sensorService;
  }
  public List<SensorTemperatura> buscarSensoresTemperatura(){
    return sensorService.traerSensoresTemperatura();
  }

  public List<SensorMovimiento> buscarSensoresMovimiento(){
    return sensorService.traerSensoresMovimiento();
  }

  public List<RegistroTemperatura> buscarRegistrosTemperatura(){
    return sensorService.traerRegistrosTemperatura();
  }

  public List<RegistroMovimiento> buscarRegistrosMovimiento(){
    return sensorService.traerRegistrosMovimiento();
  }

  public void agregarSensorTemperatura(SensorTemperatura sensor) {
    sensorService.guardarSensorTemperatura(sensor);
  }
  public void agregarSensorMovimiento(SensorMovimiento sensor) {
    sensorService.guardarSensorMovimiento(sensor);
  }
  public List<SensorTemperatura> buscarSensoresConFallaDeConexion(){
      List<SensorTemperatura> sensores = buscarSensoresTemperatura();
      List<SensorTemperatura> sensoresConFalla = new ArrayList<>();
      for(SensorTemperatura sensor :sensores){
        List<RegistroTemperatura> registros = sensor.getRegistroDeTemperatura();
        if(registros.isEmpty()){
          continue;
        }
        RegistroTemperatura ultimoRegistro = registros.get(registros.size()-1);
        long diferenciaEnMinutos = Duration.between(LocalDateTime.now(), ultimoRegistro.getFecha()).toMinutes();
        if(Math.abs(diferenciaEnMinutos) > 5){
          sensoresConFalla.add(sensor);
        }
      }
      return  sensoresConFalla;
  }

}
