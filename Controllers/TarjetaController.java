package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoAccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.PersonaEnSituacionVulnerable;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.RegistroUsoDeTarjeta;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import ar.edu.utn.frba.dds.services.IIncidenteService;
import ar.edu.utn.frba.dds.services.ITarjetaService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.Broker;
import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TarjetaController implements ICrudViewsHandler {
  private ITarjetaService tarjetaService;
  private MqttClient cliente;


  public TarjetaController(ITarjetaService tarjetaService) {
    this.tarjetaService = tarjetaService;
    String id = UUID.randomUUID().toString();
    cliente = ServiceLocator.broker().crearCliente(id);
    Broker broker = ServiceLocator.broker();
    broker.suscribir(cliente, "topicApertura", this::recibirDato);
  }


  private void recibirDato(String topic, MqttMessage mqttMessage) {
    //HeladeraId,viandaId,tarjetaId,tipoAccion,motivoApertura
    String[] mensaje = new String(mqttMessage.getPayload()).split(",");
    Heladera heladera = ServiceLocator.heladeraService().traerHeladeraPorid(Long.valueOf(mensaje[0]));
    Vianda vianda = ServiceLocator.heladeraService().traerViandaPorId(Long.valueOf(mensaje[1]));
    Tarjeta tarjeta = tarjetaService.traerTarjetaPorId(mensaje[2]);
    TipoAccion tipoAccion = TipoAccion.valueOf(mensaje[3]);
    MotivoApertura motivo = MotivoApertura.valueOf(mensaje[4]);
    RegistroUsoDeTarjeta registroUsoDeTarjeta = new RegistroUsoDeTarjeta(heladera, vianda, tipoAccion, motivo);

    if(TipoAccion.CONSUMO_VIANDA == tipoAccion) {
      PersonaEnSituacionVulnerable personaQueUsoTarjeta = ServiceLocator.personaVulnerableService().traerPersonaVulnerablePorTarjetaId(tarjeta.getId());
      tarjeta.agregarUso(registroUsoDeTarjeta, personaQueUsoTarjeta);
    }
    else {
      tarjeta.agregarUso(registroUsoDeTarjeta);
    }
  }

  public List<Tarjeta> buscarTarjetas()
  {
    return tarjetaService.traerTarjetas();
  }

  public Tarjeta buscarTarjetaPorId(String id){
    return tarjetaService .traerTarjetaPorId(id);
  }

  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {

  }

  @Override
  public void save(Context context) {

  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }
}
