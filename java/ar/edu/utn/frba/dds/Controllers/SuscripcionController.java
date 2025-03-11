package ar.edu.utn.frba.dds.Controllers;

import static java.lang.Thread.sleep;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.*;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.services.ISuscripcionService;
import ar.edu.utn.frba.dds.services.externos.obtenerCoordenadas.ObtenerCoordenadasAPI;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.Broker;
import ar.edu.utn.frba.dds.utilidades.calculadoras.CalculadoraPorArea;
import io.javalin.http.Context;

import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class SuscripcionController {
    private ISuscripcionService suscripcionService;
    private MqttClient client;

    public SuscripcionController(ISuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
        Broker broker = ServiceLocator.broker();
        String id = UUID.randomUUID().toString();
        this.client = broker.crearCliente(id);
        broker.suscribir(client, "topicApertura", this::recibirDato);
        broker.suscribir(client, "topicIncidente", this::recibirDatoIncidente);
    }

    private void recibirDatoIncidente(String topic, MqttMessage mqttMessage) {
        String[] mensaje = new String(mqttMessage.getPayload()).split(",");
        CalculadoraPorArea calculadoraPorArea = ServiceLocator.calculadoraPorArea();
        List<Heladera> heladeras = ServiceLocator.heladeraService().traerHeladeras();
        Heladera heladera = this.obtenerHeladera(Long.parseLong(mensaje[0]), heladeras);
        TipoIncidente incidente = TipoIncidente.valueOf(mensaje[1]);
        List<SubDesperfectoHeladera> subs = this.traerSubDesperfectoHeladera();
        subs = subs.stream().filter(sub -> (calculadoraPorArea.heladeraEstaEnArea(heladera, sub.getArea()))).collect(Collectors.toList());
        subs.forEach(sub -> sub.notificar(heladera, heladeras));
    }

    //HeladeraId,viandaId,tarjetaId,tipoAccion,motivoApertura
    private void recibirDato(String topic, MqttMessage mqttMessage) throws InterruptedException {
        CalculadoraPorArea calculadoraPorArea = ServiceLocator.calculadoraPorArea();
        sleep(5000);
        String[] mensaje = new String(mqttMessage.getPayload()).split(",");
        MotivoApertura motivoApertura = MotivoApertura.valueOf(mensaje[4]);
        List<Heladera> heladeras = ServiceLocator.heladeraService().traerHeladeras();
        Heladera heladera = this.obtenerHeladera(Long.parseLong(mensaje[0]), heladeras);

        if (motivoApertura.equals(MotivoApertura.RETIRO_VIANDA)) {
            Integer cantViandas = heladera.capacidadOcupada();
            List<SubQuedanNViandas> subs = this.traerSubsQuedanNViandas();

            subs = subs.stream().filter(sub -> (calculadoraPorArea.heladeraEstaEnArea(heladera, sub.getArea())
                    && sub.getNumeroViandas() >= cantViandas)).collect(Collectors.toList());
            subs.forEach(sub -> {
                sub.notificar(heladera, heladeras);
                suscripcionService.agregarSuscripcion(sub);
            });

        } else if (motivoApertura.equals(MotivoApertura.INGRESO_VIANDA)) {
            Integer cantViandas = heladera.capacidadDisponible();
            List<SubFaltanNViandas> subs = this.traerSubsFaltanNViandas();
            subs = subs.stream().filter(sub -> (calculadoraPorArea.heladeraEstaEnArea(heladera, sub.getArea())
                    && sub.getNumeroViandas() <= cantViandas)).collect(Collectors.toList());
            subs.forEach(sub -> {
                sub.notificar(heladera, heladeras);
                suscripcionService.agregarSuscripcion(sub);
            });

        }
    }

    private Heladera obtenerHeladera(Long id, List<Heladera> heladeras) {
        Heladera heladeraARetornar = null;
        for (Heladera heladera : heladeras) {
            if (heladera.getId().equals(id)) {
                heladeraARetornar = heladera;
                break;  // Salimos del ciclo una vez que encontramos la heladera
            }
        }

        if (heladeraARetornar != null) {
            heladeras.remove(heladeraARetornar);

        }
        return heladeraARetornar;
    }

    public void suscribirseAHeladerasView(Context context) {
        Map<String, Object> model = new HashMap<>();
        List<Heladera> heladeras = ServiceLocator.heladeraService().traerHeladeras();
        heladeras = heladeras.stream().filter(heladera -> heladera.getEstado() == EstadoHeladera.ACTIVA).collect(Collectors.toList());
        List<PuntoHeladera> puntos = heladeras.stream().map(Heladera::getPuntoEstrategico).collect(Collectors.toList());
        model.put("puntosHeladera", puntos);
        context.render("general/heladeras/suscribirseAHeladera.hbs", model);
    }


    public void suscribirseAHeladeras(Context context) {
        Map<String, List<String>> formParams = context.formParamMap();
        String idUsuario = context.sessionAttribute("idUsuario");
        String puntoCentral = formParams.remove("puntoCentral").get(0);
        String kmALaRedonda = formParams.remove("kmALaRedonda").get(0);


        Direccion direccion = ObtenerCoordenadasAPI.getInstancia().obtenerDireccionPorProvincia(puntoCentral, "Buenos Aires");
        Area areaABuscar = new Area(direccion, Integer.valueOf(kmALaRedonda));


        UsuarioYColaboradorController usuarioYColaboradorController = ServiceLocator.usuarioYColaboradorController();
        Colaborador colaborador = usuarioYColaboradorController.buscarColaboradorPorNombreDeUsuario(idUsuario);

        List<EstadoSuscripcion> estadosDeSuscripciones = manejarSuscripciones(formParams, colaborador, areaABuscar);


        Map<String, Object> model = new HashMap<>();
        model.put("estadoDeSuscripciones", estadosDeSuscripciones);

        context.render("modals/modalSuscripcion.hbs", model);

    }

    private List<EstadoSuscripcion> manejarSuscripciones(Map<String, List<String>> formParams, Colaborador colaborador, Area areaABuscar) {
        List<EstadoSuscripcion> estadosDeSuscripciones = new ArrayList<>();

        // Verificar cada tipo de suscripción y agregar el estado
        String suscribeAQuedanViandas = formParams.containsKey("quedanViandas") ? formParams.remove("quedanViandas").get(0) : null;
        if (suscribeAQuedanViandas != null) {
            String numeroQuedanViandas = formParams.remove("numeroQuedanViandas").get(0);
            estadosDeSuscripciones.add(this.suscripcionService.suscribirAQuedanNViandas(colaborador, numeroQuedanViandas, areaABuscar));
        }

        String suscribeAFaltanViandas = formParams.containsKey("faltanViandas") ? formParams.remove("faltanViandas").get(0) : null;
        if (suscribeAFaltanViandas != null) {
            String numeroFaltanViandas = formParams.remove("numeroFaltanViandas").get(0);
            estadosDeSuscripciones.add(this.suscripcionService.suscribirAFaltanNViandas(colaborador, numeroFaltanViandas, areaABuscar));
        }

        String desperfecto = formParams.containsKey("desperfecto") ? formParams.remove("desperfecto").get(0) : null;
        if (desperfecto != null) {
            estadosDeSuscripciones.add(this.suscripcionService.suscribirADesperfectoDeHeladeras(colaborador, areaABuscar));
        }

        return estadosDeSuscripciones;
    }

    public List<Suscripcion> traerSuscripciones() {
        return this.suscripcionService.traerSuscripciones();
    }

    public List<SubFaltanNViandas> traerSubsFaltanNViandas() {
        return this.suscripcionService.traerSubsFaltanNViandas();
    }

    public List<SubQuedanNViandas> traerSubsQuedanNViandas() {
        return this.suscripcionService.traerSubsQuedanNViandas();
    }

    public List<SubDesperfectoHeladera> traerSubDesperfectoHeladera() {
        return this.suscripcionService.traerSubsDesperfectoHeladera();
    }
}
