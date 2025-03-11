package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.RegistroTecnico;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import ar.edu.utn.frba.dds.services.serviceLocator.ITecnicoService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.GestorArchivos;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TecnicoController {

  private ITecnicoService tecnicoService;

  public TecnicoController(ITecnicoService tecnicoService) {
    this.tecnicoService = tecnicoService;
  }

  public void reporteTecnico(Context context) {
    String idUsuario = context.sessionAttribute("idUsuario");
    Tecnico tecnico = tecnicoService.traerPorUsuario(idUsuario);
    String heladeraId = context.formParam("heladera");
    String estadoHeladeraStr = context.formParam("estadoHeladera");
    String fechaTrabajo = context.formParam("fechaTrabajo");
    String descripcion = context.formParam("descripcion");
    UploadedFile reporteArchivo = context.uploadedFile("reporteArchivo");

    String path = null;
    try {
      path = "src/main/resources/archivos/img/reportes/arreglos/" + reporteArchivo.filename();
      GestorArchivos.subirArchivo(reporteArchivo, path);
    } catch (Exception e) {
      context.render("modals/modalError.hbs", Map.of("errorMessage", "error al guardar el archivo"));
      e.printStackTrace();
    }

    Heladera heladera = ServiceLocator.heladeraService().traerHeladeraPorid(Long.parseLong(heladeraId));
    EstadoHeladera estadoHeladera = EstadoHeladera.valueOf(estadoHeladeraStr);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    RegistroTecnico registroTecnico = new RegistroTecnico(descripcion,tecnico,path,estadoHeladera == EstadoHeladera.ACTIVA, LocalDate.parse(fechaTrabajo,formatter));
    heladera.setEstado(estadoHeladera);
    heladera.agregarReparacion(registroTecnico);
    ServiceLocator.heladeraService().agregarHeladera(heladera);

    Map<String, Object> model = new HashMap<>();
    model.put("successMessage", "Reporte cargada con exito");

    context.render("modals/modalSuccess.hbs", model);
  }
}
