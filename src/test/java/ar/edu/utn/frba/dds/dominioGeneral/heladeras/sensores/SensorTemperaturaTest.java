package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.ModeloHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class SensorTemperaturaTest {

  ModeloHeladera modeloHeladera = ModeloHeladera.builder().temperaturaMaxima(100F).temperaturaMinima(0F).build();
  Heladera heladera = Heladera.builder().puntoEstrategico(PuntoHeladera.builder().nombre("topicTest").build()).modelo(modeloHeladera).build();




  @Test
  public void recibirTemperatura(){
    MqttClient publisher = ServiceLocator.broker().crearCliente(UUID.randomUUID().toString());
    SensorTemperatura sensor = new SensorTemperatura(heladera);
    ServiceLocator.broker().publish(publisher,"topicTemperatura", heladera.getPuntoEstrategico().getNombre(),"15");

    for(RegistroTemperatura registroTemperatura : sensor.getRegistroDeTemperatura()){
      System.out.println(registroTemperatura.getTemperatura());
    }

    Assertions.assertEquals(sensor.getRegistroDeTemperatura().get(0).getTemperatura(),15.0);
  }


}
