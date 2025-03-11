package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores;


import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.receptores.ReceptorSensorTemperatura;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Data;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Data
@Getter
@Setter
@NoArgsConstructor
public class SensorTemperatura  {
    private List<RegistroTemperatura> registroDeTemperatura;
    private Heladera heladera;
    private ReceptorSensorTemperatura receptor;
    private MqttClient cliente;
    private String sensorId;


    public SensorTemperatura(Heladera heladera){
        registroDeTemperatura = new ArrayList<RegistroTemperatura>();
        this.sensorId = UUID.randomUUID().toString();
        this.heladera = heladera;
        receptor = new ReceptorSensorTemperatura();
        cliente = ServiceLocator.broker().crearCliente(sensorId);
        ServiceLocator.broker()
            .suscribir(cliente,"topicTemperatura",heladera.getPuntoEstrategico().getNombre().replaceAll("\\s+", ""),this::recibirDato);
    }

    public void recibirDato(String topic, MqttMessage mqttMessage) {
        String temperatura = new String(mqttMessage.getPayload());
        registrarTemperatura(temperatura);
        receptor.evaluar(heladera, temperatura);
    }

    public void registrarTemperatura(String temperatura){
        RegistroTemperatura reg = RegistroTemperatura.builder().fecha(LocalDateTime.now()).temperatura(Double.parseDouble(temperatura)).heladera(heladera).build();
        registroDeTemperatura.add(reg);

    }


}
