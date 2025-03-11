package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.receptores.ReceptorSensorMovimiento;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Table;
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
public class SensorMovimiento  {
    private List<RegistroMovimiento> registroDeMovimiento;
    private Heladera heladera;
    private ReceptorSensorMovimiento receptor;
    private MqttClient cliente;
    private String sensorId;

    public SensorMovimiento(Heladera heladera){
        registroDeMovimiento = new ArrayList<RegistroMovimiento>();
        this.sensorId = UUID.randomUUID().toString();
        this.heladera = heladera;
        receptor = new ReceptorSensorMovimiento();
        cliente = ServiceLocator.broker().crearCliente(sensorId);

        ServiceLocator.broker()
            .suscribir(cliente,"topicMovimiento",heladera.getPuntoEstrategico().getNombre(),this::recibirDato);
    }

  public void recibirDato(String topic, MqttMessage mqttMessage) {
      String movimiento = new String(mqttMessage.getPayload());
      registrarMovimiento(movimiento);
      receptor.evaluar(heladera, movimiento);
  }

    public void registrarMovimiento(String movimiento){
        RegistroMovimiento reg = RegistroMovimiento.builder().fecha(LocalDateTime.now()).descripcionMovimiento(movimiento).heladera(heladera).build();
        registroDeMovimiento.add(reg);
    }

}
