package ar.edu.utn.frba.dds.cronJobs;

import ar.edu.utn.frba.dds.Controllers.SensorController;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.Accionador;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando.CambiarEstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando.Comando;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando.LanzarAlerta;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.SensorTemperatura;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class MainComprobarConexionesSensores implements Runnable{

  @Override
  public void run(){
    SensorController controller = ServiceLocator.sensorController();
    List<SensorTemperatura> sensoresDeTemperaturaConFalla  = controller.buscarSensoresConFallaDeConexion();
    List<Comando> comandos = new ArrayList<>();
    comandos.add(new LanzarAlerta());
    comandos.add(new CambiarEstadoHeladera());
    Accionador accionador = Accionador.builder().comandos(comandos).build();
    for(SensorTemperatura sensor : sensoresDeTemperaturaConFalla){
      accionador.sucedeIncidente(sensor.getHeladera(), TipoIncidente.FALLA_CONEXION);
    }
    }

  }

