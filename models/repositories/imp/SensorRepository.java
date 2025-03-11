package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroTemperatura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorTemperatura;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.ISensorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.Session;

public class SensorRepository implements ISensorRepository {
  private List<SensorMovimiento> sensoresMovimientos;
  private List<SensorTemperatura> sensoresTemperaturas;


  public SensorRepository() {
    sensoresMovimientos = new ArrayList<>();
    sensoresTemperaturas = new ArrayList<>();
  }
  @Override
  public List<SensorTemperatura> buscarSensoresTemperatura() {
    return sensoresTemperaturas;
  }

  @Override
  public List<SensorMovimiento> buscarSensoresMovimeinto() {
    return sensoresMovimientos;
  }

  @Override
  public Optional<SensorTemperatura> buscarSensorTemperaturaPorId(String id) {
    return sensoresTemperaturas.stream().filter(sensorTemperatura -> Objects.equals(sensorTemperatura.getSensorId(), id)).findFirst();
  }

  @Override
  public Optional<SensorMovimiento> buscarSensorMovimeintoPorId(String id) {
    return sensoresMovimientos.stream().filter(sensorMovimiento-> Objects.equals(sensorMovimiento.getSensorId(), id)).findFirst();
  }

  @Override
  public void guardarSensorTemperatura(SensorTemperatura sensorTemperatura) {
    sensoresTemperaturas.add(sensorTemperatura);
  }


  @Override
  public void guardarSensorMovimiento(SensorMovimiento sensorMovimiento) {
    sensoresMovimientos.add(sensorMovimiento);
  }


}
