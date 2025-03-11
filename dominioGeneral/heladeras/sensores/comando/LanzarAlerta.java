package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.comando;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;

public class LanzarAlerta implements Comando{
    MqttClient client;

    public LanzarAlerta(){
        String id = UUID.randomUUID().toString();
        this.client = ServiceLocator.broker().crearCliente(id);//dejamos el metodo o chau?
    }
    @Override
    public void accionar(Heladera heladera, TipoIncidente incidente) {
        String mensaje =  heladera.getId() + "," + incidente.toString() ;
        ServiceLocator.broker().publish(client, "topicIncidente", mensaje);
    }
}
