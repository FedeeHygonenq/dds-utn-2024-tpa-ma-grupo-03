package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoAccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.IntentoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.SolicitudDeApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.PermisoAperturaException;
import ar.edu.utn.frba.dds.services.IHeladerasService;
import ar.edu.utn.frba.dds.services.externos.obtenerCoordenadas.ObtenerCoordenadasAPI;
import ar.edu.utn.frba.dds.utilidades.Broker;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;

import java.util.*;

import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import io.javalin.http.Context;

import java.util.stream.Collectors;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Data

public class HeladeraController {
    private MqttClient cliente;
    private IHeladerasService heladerasService;

    public HeladeraController(IHeladerasService heladerasService) {
        this.heladerasService = heladerasService;
        String id = UUID.randomUUID().toString();
        Broker broker = ServiceLocator.broker();
        cliente = ServiceLocator.broker().crearCliente(id);
        broker.suscribir(cliente, "topicApertura", this::procesarApertura);
        broker.suscribir(cliente, "topicIntentoApertura", this::recibirDato);
    }

    private void procesarApertura(String s, MqttMessage mqttMessage) {
        //HeladeraId,viandaId,tarjetaId,tipoAccion,motivoApertura
        String[] mensaje = new String(mqttMessage.getPayload()).split(",");
        Heladera heladera = heladerasService.traerHeladeraPorid(Long.valueOf(mensaje[0]));
        Vianda vianda = heladerasService.traerViandaPorId(Long.valueOf(mensaje[1]));
        Tarjeta tarjeta = ServiceLocator.tarjetaService().traerTarjetaPorId(mensaje[2]);
        TipoAccion tipoAccion = TipoAccion.valueOf(mensaje[3]);
        MotivoApertura motivo = MotivoApertura.valueOf(mensaje[4]);

        if (motivo == MotivoApertura.RETIRO_VIANDA) {
            heladera.retirarVianda(vianda);
            if (tipoAccion == TipoAccion.CONSUMO_VIANDA) {
                vianda.setEstaEntregada(true);
                heladerasService.guardarVianda(vianda);
            }
        } else {
            heladera.ingresarVianda(vianda);
        }
        heladerasService.agregarHeladera(heladera);
    }

    private void recibirDato(String topic, MqttMessage mqttMessage) {
        if (Objects.equals(topic, LectorProperties.getStringPropertie("topicIntentoApertura"))) {
            //MotivoApertura, TipoAccion, HeladeraId, TarjetaId
            String[] mensaje = new String(mqttMessage.getPayload()).split(",");
            verificarIntentoApertura(mensaje);
        }
    }

    private void verificarIntentoApertura(String[] mensaje) {

        try {
            Heladera heladera = heladerasService.traerHeladeraPorid(Long.valueOf(mensaje[2]));
            Tarjeta tarjeta = ServiceLocator.tarjetaController().buscarTarjetaPorId(mensaje[3]);
            TipoAccion tipoAccion = TipoAccion.valueOf(mensaje[1]);
            MotivoApertura motivo = MotivoApertura.valueOf(mensaje[0]);
            heladera.recibirIntentoApertura(new IntentoApertura(tarjeta, heladera, tipoAccion, motivo));
            heladerasService.agregarHeladera(heladera);
            System.out.println("Se recibio un intento de apertura");
            accionarSiFueContribucion(tipoAccion, motivo, tarjeta);
        } catch (PermisoAperturaException e) {
            e.printStackTrace();
        }

    }

    private void accionarSiFueContribucion(TipoAccion tipoAccion, MotivoApertura motivo, Tarjeta tarjeta) {
        if (tipoAccion == TipoAccion.DONAR_VIANDA || (tipoAccion == TipoAccion.DISTRIBUIR_VIANDA && motivo == MotivoApertura.INGRESO_VIANDA)) {
            String codigo = UUID.randomUUID().toString();
            ServiceLocator.contribucionService().agregarCodigo(codigo);
            System.out.println("Se genero un codigo de contribucion");
            enviarCodigoAUsuario(tarjeta, codigo);
        }
    }


    private void enviarCodigoAUsuario(Tarjeta tarjeta, String codigo) {
        Colaborador colaborador = ServiceLocator.colaboradorService().traerColaboradorPorTarjeta(tarjeta.getId());
        Notificacion notificacion = new Notificacion("Su código para hacer la carga de la contribucio en la web es: " + codigo, "Código para cargar la contribución");
        ServiceLocator.notificador().enviar(List.of(colaborador), notificacion);
        System.out.println("Se envio el codigo de contribucion");
    }


    public void registrarSolicitudApertura(String[] mensaje) {
        TipoAccion tipoAccion = TipoAccion.valueOf(mensaje[0]);
        MotivoApertura motivo = MotivoApertura.valueOf(mensaje[1]);
        Heladera heladera = heladerasService.traerHeladeraPorid(Long.valueOf(mensaje[2]));
        Tarjeta tarjeta = ServiceLocator.tarjetaController().buscarTarjetaPorId(mensaje[3]);
        if (tarjeta == null) {
            throw new NoSuchElementException("La tarjeta no existe");
        }
        try {
            SolicitudDeApertura solicitud = new SolicitudDeApertura(tarjeta, heladera, motivo, tipoAccion);
            heladera.agregarSolicitudAperturaEnHeladera(solicitud);
            heladerasService.agregarSolicitudApertura(solicitud);
            heladerasService.agregarHeladera(heladera);

        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Heladera> buscarHeladeras() {
        return heladerasService.traerHeladeras();
    }

    public void limpiarSolicitudesCaducadas() {
        heladerasService.limpiarSolicitudesCaducadas();
    }

    public List<PuntoHeladera> buscarPuntosHeladera() {
        return heladerasService.traerPuntosHeladera();
    }

    public void darDeBajaHeladeraView(Context context) {
        Map<String, List<Heladera>> model = new HashMap<>();
        Long id = context.sessionAttribute("idColaborador");
        List<Heladera> heladeras = ServiceLocator.colaboradorService().traerHeladerasDeColaboradorPorId(id);
        heladeras = heladeras.stream().filter(heladera -> heladera.getEstado() != EstadoHeladera.DE_BAJA).collect(Collectors.toList());
        model.put("heladeras", heladeras);

        context.render("general/heladeras/bajaHeladera.hbs", model);
    }

    public void darDeBajaHeladera(Context context) {
        Long id = context.sessionAttribute("idColaborador");
        Long idHeladera = Long.valueOf(Objects.requireNonNull(context.formParam("heladeraId")));

        try {
            heladerasService.darDeBajaHeladeraPorId(idHeladera);
        } catch (NoSuchElementException e) {
            Map<String, String> model = new HashMap<>();
            model.put("message", e.getMessage());
            context.render("modals/modalError.hbs", model);
        }
        Map<String, String> model = new HashMap<>();
        String mensaje = "Se dio de baja la heladera";
        model.put("message", mensaje);

        context.render("modals/modalSuccess.hbs", model);
    }

    public void solicitarAperturaView(Context context) {
        List<Heladera> heladeras = this.heladerasService.traerHeladeras();
        TipoAccion[] tiposAccion = TipoAccion.values();
        MotivoApertura[] motivosApertura = MotivoApertura.values();
        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);
        model.put("tiposAccion", tiposAccion);
        model.put("motivosApertura", motivosApertura);
        context.render("colaboradorHumano/solicitudApertura.hbs", model);
    }


    public void accionSobreHeladeraView(Context context) {

        context.render("general/heladeras/accionSobreHeladera.hbs");
    }

    public void consultaHeladerasView(Context context) {
        Long colaboradorId = context.sessionAttribute("idColaborador");

        // Llama al servicio para obtener heladeras
        List<Heladera> heladeras = ServiceLocator.colaboradorService().traerHeladerasDeColaboradorPorId(colaboradorId);

        // Si heladeras es nulo, inicializa como lista vacía
        if (heladeras == null) {
            heladeras = new ArrayList<>();
        }

        // Prepara el modelo para la vista
        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);

        context.render("general/heladeras/consultasHeladeras.hbs", model);

    }

    public void consultaHeladeras(Context context) {
        Long idHeladera = Long.valueOf(Objects.requireNonNull(context.formParam("heladera")));
        String tipoConsulta = context.formParam("tipoConsulta");
        Heladera heladera = heladerasService.traerHeladeraPorid(idHeladera);
        Map<String, Object> model = new HashMap<>();
        if (tipoConsulta.equals("General")) {
            String ubicacion = ObtenerCoordenadasAPI.getInstancia().obtenerUbicacion(heladera.getPuntoEstrategico().getDireccion());
            List<Vianda> viandas = heladera.getViandas();
            model.put("viandas", viandas);
            model.put("ubicacion", ubicacion);
        } else if (tipoConsulta.equals("Tecnica")) {
            Float tempMaxima = heladera.getModelo().getTemperaturaMaxima();
            Float tempMinima = heladera.getModelo().getTemperaturaMinima();
            String estado = heladera.getEstado().toString();
            Integer capacidad = heladera.getModelo().getCapacidadEnViandas();

            model.put("tempMaxima", tempMaxima);
            model.put("tempMinima", tempMinima);
            model.put("estado", estado);
            model.put("capacidad", capacidad);
        } else if (tipoConsulta.equals("Fallas y reparaciones")) {
            List<RegistroIncidente> registrosIncidentes = ServiceLocator.incidenteService().traerIncidentesPorHeladera(heladera);
            model.put("registrosIncidentes", registrosIncidentes);
            model.put("reparaciones", heladera.getReparacion());
        }
        context.render("general/heladeras/mensajeConsultaHeladera.hbs", model);
    }


    public void solicitarApertura(Context context) {
        Map<String, List<String>> formParams = context.formParamMap();
        String idHeladera = context.formParam("heladera");
        String tipoAccionStr = context.formParam("accion");
        String motivoAperturaStr = context.formParam("motivoApertura");
        String idTarjeta = context.formParam("tarjeta");
        try {
            registrarSolicitudApertura(new String[]{tipoAccionStr, motivoAperturaStr, idHeladera, idTarjeta});
        } catch (NoSuchElementException e) {
            Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", e.getMessage());
            context.status(400);
            context.render("modals/modalError.hbs", model);
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("successMessage", "Solicitud cargada con exito");

        context.render("modals/modalSuccess.hbs", model);
    }


}
