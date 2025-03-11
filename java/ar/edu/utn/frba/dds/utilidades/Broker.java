package ar.edu.utn.frba.dds.utilidades;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Broker {

  public MqttClient crearCliente(String id) {
    MqttClient cliente;
    try {
      String urlBroker = LectorProperties.getStringPropertie("urlBroker");
      cliente = new MqttClient(urlBroker, id, new MemoryPersistence());
      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      connOpts.setUserName(LectorProperties.getStringPropertie("usuario"));
      connOpts.setPassword(LectorProperties.getStringPropertie("contrasenia").toCharArray());
      cliente.connect(connOpts);
    } catch (
        MqttException e) {
      throw new RuntimeException(e);
    }
    return cliente;//cliente conectado..
  }

  public void publish(MqttClient cliente, String topic, String mensaje) {
    MqttMessage mqttMensaje = new MqttMessage(mensaje.getBytes());
    mqttMensaje.setQos(2);
    try {
      cliente.publish(LectorProperties.getStringPropertie(topic), mqttMensaje);
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }

  public void publish(MqttClient cliente, String claveTopic, String restoTopic, String mensaje) {
    MqttMessage mqttMensaje = new MqttMessage(mensaje.getBytes());
    mqttMensaje.setQos(2);
    try {
      cliente.publish(LectorProperties.getStringPropertie(claveTopic) + restoTopic, mqttMensaje);
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }

  public void suscribir(MqttClient client, String topic, IMqttMessageListener metodo) {
    try {
      {
        client.subscribe(LectorProperties.getStringPropertie(topic), metodo);
      }
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }

  public void suscribir(MqttClient client, String claveTopic, String restoTopic, IMqttMessageListener metodo) {
    try {
      {
        client.subscribe(LectorProperties.getStringPropertie(claveTopic) + restoTopic, metodo);
      }
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }


}


