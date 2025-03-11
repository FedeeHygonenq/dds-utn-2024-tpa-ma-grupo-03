package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.Controllers.SensorController;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroTemperatura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorTemperatura;
import java.util.List;
import java.util.Optional;

public interface ISensorRepository {

   List<SensorTemperatura> buscarSensoresTemperatura();
   List<SensorMovimiento> buscarSensoresMovimeinto();

   Optional<SensorTemperatura> buscarSensorTemperaturaPorId(String id);

   Optional<SensorMovimiento> buscarSensorMovimeintoPorId(String id);

   void guardarSensorTemperatura(SensorTemperatura sensorTemperatura);

   void guardarSensorMovimiento(SensorMovimiento sensorMovimiento);

}
