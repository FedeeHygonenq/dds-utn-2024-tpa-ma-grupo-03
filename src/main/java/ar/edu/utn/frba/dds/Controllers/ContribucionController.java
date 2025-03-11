package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.Controllers.dtos.PuntoRecomendadoOut;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.DistribucionDeViandas;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.DonacionDeDinero;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.DonacionDeViandas;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.BeneficiosPorPuntos;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.hacerseCargoHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.MotivoDistribucion;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoAccion;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.ModeloHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.productosYServicios.ItemDeCatalogo;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FraudeException;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.GuardarArchivoException;
import ar.edu.utn.frba.dds.services.IColaboradorService;
import ar.edu.utn.frba.dds.services.externos.obtenerCoordenadas.ObtenerCoordenadasAPI;
import ar.edu.utn.frba.dds.services.IContribucionService;
import ar.edu.utn.frba.dds.services.imp.HeladeraService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.Broker;
import ar.edu.utn.frba.dds.utilidades.GestorArchivos;
import io.javalin.http.Context;

import io.javalin.http.UploadedFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.eclipse.paho.client.mqttv3.MqttClient;

public class ContribucionController {

  private IContribucionService contribucionService;
  private IColaboradorService colaboradorService;
  private MqttClient client;

  public ContribucionController(IContribucionService contribucionService, IColaboradorService colaboradorService) {
    this.contribucionService = contribucionService;
    this.colaboradorService = colaboradorService;
    Broker broker = ServiceLocator.broker();
    String id = UUID.randomUUID().toString();
    this.client = broker.crearCliente(id);
  }

  public void contribucionView(Context context) {
    Map<String, Object> model = new HashMap<>();
    String tipoUsuario = context.sessionAttribute("tipoRol");
    model.put("tipoUsuario", tipoUsuario);

    List<Heladera> heladeras = ServiceLocator.heladeraService().traerHeladeras();
    Map<String, Double> multiplicadores = contribucionService.obtenerMultiplicadores();
    model.put("multiplicadores", multiplicadores);

    List<ModeloHeladera> modelosHeladeras = ServiceLocator.heladeraService().traerModelosDeHeladeras();
    List<String> motivosDitribucion = new ArrayList<>();

    for (MotivoDistribucion motivoDistribucion : MotivoDistribucion.values()) {
      motivosDitribucion.add(motivoDistribucion.name());
    }
    model.put("heladeras", heladeras);
    model.put("modelosHeladeras", modelosHeladeras);
    model.put("motivosDistribucion", motivosDitribucion);
    context.render("general/contribucion.hbs", model);

  }

  public void consultarPuntoRecomendado(Context context) {
    Map<String, Object> model = new HashMap<>();
    Map<String, List<String>> formParams = context.queryParamMap();

    String kmALaRedonda = formParams.remove("kms").get(0);
    String latitud = formParams.remove("latitud-central").get(0);
    String longitud = formParams.remove("longitud-central").get(0);

    Direccion direccion = new Direccion(Double.valueOf(longitud), Double.valueOf(latitud));
    ObtenerCoordenadasAPI obtenerCoordenadasAPI = ObtenerCoordenadasAPI.getInstancia();
    List<PuntoHeladera> puntosRecomendados = contribucionService.buscarPuntosRecomendados(new Area(direccion, Integer.valueOf(kmALaRedonda)));

    List<PuntoRecomendadoOut> coordenadas = new ArrayList<>();
    for (PuntoHeladera puntoHeladera : puntosRecomendados) {
      coordenadas.add(new PuntoRecomendadoOut(puntoHeladera.getDireccion(), obtenerCoordenadasAPI.obtenerUbicacion(puntoHeladera.getDireccion())));
    }
    model.put("puntosRecomendados", coordenadas);
    context.render("colaboradorJuridico/respuestaPuntoRecomendado.hbs", model);
  }

  public void contribuir(Context context) {

    String idUsuario = context.sessionAttribute("idUsuario");

    Map<String, List<String>> formParams = context.formParamMap();

    String tipoContribucion = formParams.remove("formaContribucion").get(0);

    Colaborador colaborador = colaboradorService.traerColaboradorPorNombreDeUsuario(idUsuario);

    HeladeraService heladeraService = ServiceLocator.heladeraService();
    switch (tipoContribucion) {
      case "dinero":
        String monto = formParams.remove("cantidadADonar").get(0);
        String frecuencia = formParams.remove("frecuencia").get(0);
        this.agregarContribucionDeDinero(Double.valueOf(monto), Integer.valueOf(frecuencia), colaborador);
        break;
      case "vianda": {
        String codigoContribucion = formParams.remove("codigoContribucion").get(0);
        contribucionService.comprobarCodigo(codigoContribucion);
        String descripcion = formParams.remove("vianda").get(0);
        String heladeraDonacionId = formParams.remove("heladeraDestino").get(0);
        Heladera heladeraDonacion = heladeraService.traerHeladeraPorid(Long.valueOf(heladeraDonacionId));
        String fechaCaducidad = formParams.remove("fechaCaducidad").get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String calorias = formParams.remove("calorias").get(0);
        String peso = formParams.remove("pesoVianda").get(0);
        Vianda  vianda = this.agregarContribucionDeVianda(descripcion, LocalDate.parse(fechaCaducidad, formatter), Integer.valueOf(calorias), Double.valueOf(peso), heladeraDonacion, colaborador);
        ServiceLocator.broker().publish(client, "topicApertura", String.format("%d,%d,%s,%s,%s", heladeraDonacion.getId(), vianda.getId(), colaborador.tarjetaDeColaborador().getId(), TipoAccion.DONAR_VIANDA.toString(), MotivoApertura.INGRESO_VIANDA.toString()));
        break;
      }
      case "distribucion": {
        String codigoContribucion = formParams.remove("codigoContribucion").get(0);
        contribucionService.comprobarCodigo(codigoContribucion);
        String heladeraOrigenId = formParams.remove("heladeraOrigen").get(0);
        String heladeraDestinoId = formParams.remove("heladeraDestino").get(0);
        List<String> viandasStr = formParams.remove("vianda");
        verificarFraude(heladeraOrigenId, heladeraDestinoId, viandasStr);
        List<Vianda> viandas = viandasStr.stream().map(vianda -> ServiceLocator.heladeraService().traerViandaPorId(Long.valueOf(vianda))).collect(Collectors.toList());
        Heladera heladeraOrigen = heladeraService.traerHeladeraPorid(Long.valueOf(heladeraOrigenId));
        Heladera heladeraDestino = heladeraService.traerHeladeraPorid(Long.valueOf(heladeraDestinoId));
        String motivoDistribucion = formParams.remove("motivoDistribucion").get(0);
        this.agregarContribucionDeDistribucion(heladeraOrigen, heladeraDestino, viandas, MotivoDistribucion.valueOf(motivoDistribucion), colaborador);
        viandas.forEach(vianda -> ServiceLocator.broker().publish(client, "topicApertura", String.format("%d,%d,%s,%s,%s", heladeraOrigen.getId(), vianda.getId(), colaborador.tarjetaDeColaborador().getId(), TipoAccion.DISTRIBUIR_VIANDA.toString(), MotivoApertura.RETIRO_VIANDA.toString())));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        viandas.forEach(vianda -> ServiceLocator.broker().publish(client, "topicApertura", String.format("%d,%d,%s,%s,%s", heladeraDestino.getId(), vianda.getId(), colaborador.tarjetaDeColaborador().getId(), TipoAccion.DISTRIBUIR_VIANDA.toString(), MotivoApertura.INGRESO_VIANDA.toString())));
        break;
      }
      case "hacerseCargoHeladera":
        String direccionStr = formParams.remove("direccion").get(0);
        String nombre = formParams.remove("nombre").get(0);
        String modeloId = formParams.remove("modelo").get(0);
        ModeloHeladera modelo = ServiceLocator.heladeraService().traerModeloPorId(Long.valueOf(modeloId));

        this.agregarContribucionDeHacerseCargoHeladera(direccionStr, nombre, modelo, colaborador);

        break;
      case "beneficio":
        String ofertaStr = formParams.remove("nombreOferta").get(0);
        String stock = formParams.remove("stockOferta").get(0);
        List<String> nombreItems = formParams.remove("nombreItemCatalogo");
        List<String> costoItems = formParams.remove("precioItemCatalogo");
        List<String> cantItems = formParams.remove("cantidadItemCatalogo");
        List<ItemDeCatalogo> items = new ArrayList<>();
        for (int i = 0; i < nombreItems.size(); i++) {
          ItemDeCatalogo item = new ItemDeCatalogo(nombreItems.get(0), Float.valueOf(costoItems.get(i)), Integer.valueOf(cantItems.get(i)));
          items.add(item);
        }
        UploadedFile imagenItem = context.uploadedFile("imagenItem");
        String path;
        try {
          path = "src/main/resources/archivos/img/catalogo/" + imagenItem.filename();
          GestorArchivos.subirArchivo(imagenItem, path);
        } catch (GuardarArchivoException e) {
          Map<String, Object> model = new HashMap<>();
          model.put("errorMessage", e.getMessage());
          context.render("modals/modalError.hbs", model);
          return;
        }

        this.agregarContribucionDeBeneficio(ofertaStr, items, Integer.valueOf(stock), path, imagenItem.filename(), colaborador);

    }
    Map<String, Object> model = new HashMap<>();
    model.put("successMessage", "Contribucion cargada con exito");

    context.render("modals/modalSuccess.hbs", model);

  }

  private void verificarFraude(String heladeraOrigenId, String heladeraDestinoId, List<String> viandasStr) {
    if(heladeraOrigenId.equals(heladeraDestinoId)) {
      throw new FraudeException("La heladera de origen y destino no pueden ser las mismas");
    }
    if(viandasStr == null || viandasStr.isEmpty() ) {
      throw new FraudeException("Debe seleccionar al menos una vianda");
    }
    if(viandasStr.contains("Seleccioná una opción")) {
      throw new FraudeException("Debe seleccionar al menos una vianda en cada opción");
    }
    Set<String> conjunto = new HashSet<>();
    for (String elemento : viandasStr) {
      if (!conjunto.add(elemento)) {
        throw new FraudeException("No se puede seleccionar la misma vianda dos veces");
      }
    }

  }

  private void agregarContribucionDeDinero(Double monto, Integer frecuencia, Colaborador colaborador) {
    DonacionDeDinero donacionDeDinero = new DonacionDeDinero(monto, frecuencia, colaborador);
    colaborador.agregarContribucion(donacionDeDinero);
    colaboradorService.guardarOActualizarColaborador(colaborador);
  }

  private Vianda agregarContribucionDeVianda(String descripcion, LocalDate fechaCaduciadad, Integer calorias, Double peso, Heladera heladera, Colaborador colaborador) {
    Vianda vianda = new Vianda(descripcion, fechaCaduciadad, calorias, peso, false);
    DonacionDeViandas donacionDeViandas = new DonacionDeViandas(vianda, colaborador);
    heladera.ingresarVianda(vianda);
    ServiceLocator.heladeraService().agregarHeladera(heladera);
    colaborador.agregarContribucion(donacionDeViandas);
    colaboradorService.guardarOActualizarColaborador(colaborador);
    return vianda;
  }

  private void agregarContribucionDeDistribucion(Heladera heladeraOrigen, Heladera heladeraDestino, List<Vianda> viandas , MotivoDistribucion motivoDistribucion, Colaborador colaborador) {
    DistribucionDeViandas distribucionDeViandas = new DistribucionDeViandas(heladeraOrigen, heladeraDestino, viandas.size(), motivoDistribucion, colaborador);
    colaborador.agregarContribucion(distribucionDeViandas);
    colaboradorService.guardarOActualizarColaborador(colaborador);
  }

  private void agregarContribucionDeHacerseCargoHeladera(String direccionStr, String nombre, ModeloHeladera modelo, Colaborador colaborador) {
    Direccion direccion = ObtenerCoordenadasAPI.getInstancia().obtenerDireccionPorProvincia(direccionStr, "Buenos Aires");
    PuntoHeladera puntoHeladera = new PuntoHeladera(nombre, direccion);
    Heladera heladera = new Heladera(puntoHeladera, modelo);
    ServiceLocator.sensorService().agregarSensoresAHeladera(heladera);
    HacerseCargoDeHeladera hacerseCargoDeHeladera = new HacerseCargoDeHeladera(heladera, colaborador);
    colaborador.agregarContribucion(hacerseCargoDeHeladera);
    colaboradorService.guardarOActualizarColaborador(colaborador);
  }

  private void agregarContribucionDeBeneficio(String nombre, List<ItemDeCatalogo> items, Integer stock, String path, String nombreImagen, Colaborador colaborador) {
    Oferta oferta = new Oferta(nombre, path, nombreImagen, items, stock);
    colaborador.agregarContribucion(new BeneficiosPorPuntos(colaborador, List.of(oferta)));
    colaboradorService.guardarOActualizarColaborador(colaborador);
  }


}
