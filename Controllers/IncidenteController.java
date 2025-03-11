package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.services.IIncidenteService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.GestorArchivos;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import io.javalin.http.UploadedFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


import io.javalin.http.Context;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class IncidenteController {
    private MqttClient cliente;
    private IIncidenteService incidenteService;

    public IncidenteController(IIncidenteService incidenteService) {
        this.incidenteService = incidenteService;
        String id = UUID.randomUUID().toString();
        cliente = ServiceLocator.broker().crearCliente(id);
        ServiceLocator.broker().suscribir(cliente, "topicIncidente", this::recibirDato);
    }


    private void recibirDato(String topic, MqttMessage mqttMessage) {
        // "9,Temperatura" id, tipo incidente
        String[] mensaje = new String(mqttMessage.getPayload()).split(",");
        TipoIncidente tipoIncidente = TipoIncidente.valueOf(mensaje[1]);
        String descripcion = switch (tipoIncidente) {
            case TEMPERATURA -> "Temperatura fuera de rango";
            case FRAUDE -> "Fraude";
            case FALLA_CONEXION -> "Falla de conexion";
            default -> "";
        };
        try {
            Heladera heladera = incidenteService.buscarHeladeraPorId(Long.valueOf(mensaje[0]));

            registrarIncidente(tipoIncidente, heladera, descripcion);
            avisarTecnico(heladera);

        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }


    public void registrarIncidente(TipoIncidente tipoIncidente, Heladera heladera, String descripcion) {
        RegistroIncidente registro = RegistroIncidente.builder()
                .incidente(tipoIncidente)
                .heladera(heladera)
                .fecha(LocalDateTime.now())
                .descripcion(descripcion)
                .build();

        incidenteService.agregarIncidente(registro);
    }

    public void avisarTecnico(Heladera heladera) {
        List<Tecnico> tecnicos = incidenteService.traerTecnicos();
        List<Tecnico> tecnicosQueCubrenHeladera = ServiceLocator.calculadoraPorArea().tecnicosQueCubrenHeladera(tecnicos, heladera);
        Tecnico tecnico = heladera.tecnicoMasCercano(tecnicosQueCubrenHeladera);
        Notificacion notificacion = Notificacion.builder()
                .asunto("Una heladera necesita reparacion")
                .mensaje("La heladera " + heladera.getPuntoEstrategico().getNombre() + " necesita ser reparada y usted es el tecnico seleccionado para ello.")
                .build();
        ServiceLocator.notificador().enviar(tecnico, notificacion);
    }

    public List<RegistroIncidente> buscarIncidentes() {
        return incidenteService.traerIncidentes();
    }


    public void reporteDeFallaView(Context context) {
        List<Heladera> heladeras = ServiceLocator.heladeraService().traerHeladeras();
        Map<String, Object> model = new HashMap<>();
        String tipoUsuario = context.sessionAttribute("tipoRol");
        List<String> estadosHeladera = Arrays.stream(EstadoHeladera.values()).map(Enum::name).collect(Collectors.toList());
        model.put("estados", estadosHeladera);
        model.put("tipoUsuario", tipoUsuario);
        model.put("heladeras", heladeras);
        context.render("general/heladeras/reporteFalla.hbs", model);// luego abria que agregar caso de error por si no hay heladeras disponibles.
    }

    public void reporteDeFallaNuevo(Context context) {
        Long colabId = context.sessionAttribute("idColaborador");
        Colaborador colaborador = ServiceLocator.colaboradorService().traerColaboradorPorId(colabId);
        String id_heladera = context.formParam("heladera");
        UploadedFile archivo = context.uploadedFile("reporteArchivo");
        String nombre_archivo = archivo.filename();
        String descripcion = context.formParam("descripcion");
        String path = null;
        try {
            path = "src/main/resources/archivos/img/reportes/fallas/" + nombre_archivo;
            GestorArchivos.subirArchivo(archivo, path);

        } catch (Exception e) {
            context.render("modals/modalError.hbs", Map.of("errorMessage", "error al guardar el archivo"));
            e.printStackTrace();
        }

        Heladera heladera = ServiceLocator.heladeraService().traerHeladeraPorid(Long.valueOf(id_heladera));
        if (heladera != null) {
            RegistroIncidente registroIncidente = new RegistroIncidente(TipoIncidente.FALLA_TECNICA, heladera, descripcion, path, colaborador);
            incidenteService.agregarIncidente(registroIncidente);
            heladera.setEstado(EstadoHeladera.FALLA_TECNICA);
            ServiceLocator.heladeraService().agregarHeladera(heladera);
            avisarTecnico(heladera);
            Map<String, Object> model = new HashMap<>();
            model.put("successMessage", "Reporte cargada con exito");

            context.render("modals/modalSuccess.hbs", model);
        } else {
            context.render("modals/modalError.hbs", Map.of("errorMessage", "Heladera no encontrada"));
        }

    }
}
