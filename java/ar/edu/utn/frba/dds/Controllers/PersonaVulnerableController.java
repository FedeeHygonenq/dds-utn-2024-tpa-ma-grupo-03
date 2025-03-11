package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.RegistroDePersonaVulnerable;
import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.CampoFormulario;
import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.CampoRespondido;
import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.FormularioRespondido;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoDocumento;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoPersona;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.PersonaEnSituacionVulnerable;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.EdadInvalidaException;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.LocalidadInvalidaException;
import ar.edu.utn.frba.dds.services.IColaboradorService;
import ar.edu.utn.frba.dds.services.externos.obtenerCoordenadas.ObtenerCoordenadasAPI;
import ar.edu.utn.frba.dds.services.imp.PersonaVulnerableService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import io.javalin.http.Context;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class PersonaVulnerableController {

  private PersonaVulnerableService personaVulnerableService;

  public PersonaVulnerableController(PersonaVulnerableService personaVulnerableService) {
    this.personaVulnerableService = personaVulnerableService;
  }

  public void formularioPersonaVulnerableView(Context context) {

    Map<String, Object> model = new HashMap<>();
    List<String> tiposDocumento = tiposDocumento();
    model.put("tiposDocumento", tiposDocumento);
    context.render("personaVulnerable/registroPersonaVulnerable.hbs", model);
  }

  public void registroExitosoView(Context context) {
    context.render("personaVulnerable/registroExitoso.hbs");
  }


  private PersonaEnSituacionVulnerable crearPersonaVulnerable(Map<String, List<String>> formParams)
  {
    String nombre = formParams.remove("nombre").get(0);
    String apellido = formParams.remove("apellido").get(0);

    String tipoDocumentoStr = formParams.remove("tipoDocumento").get(0);
    TipoDocumento tipoDocumento = TipoDocumento.valueOf(tipoDocumentoStr.toUpperCase());
    String nroDocumento = formParams.remove("nroDocumento").get(0);

    String fechaNacimientoStr = formParams.remove("fechaNacimiento").get(0);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);

    if (!esFechaAnterior(fechaNacimiento)) {
      throw new EdadInvalidaException("La fecha de nacimiento no puede ser posterior a la fecha actual");
    }

    Direccion direccion = this.registrarDireccion(formParams);

    String nroTarjeta = formParams.remove("nroTarjeta").get(0);
    Tarjeta tarjeta = new Tarjeta(nroTarjeta, TipoPersona.PersonaVulnerable);
    List<PersonaEnSituacionVulnerable> menoresACargo = this.registrarHijos(formParams, direccion);

    FormularioRespondido formularioRespondido = this.registrarFormularioRespondido(formParams);

    PersonaEnSituacionVulnerable personaEnSituacionVulnerable = new PersonaEnSituacionVulnerable(formularioRespondido, nombre, apellido, fechaNacimiento, direccion, tipoDocumento, nroDocumento, tarjeta, menoresACargo);

    //this.personaVulnerableService.agregarPersonaVulnerable(personaEnSituacionVulnerable);

    return personaEnSituacionVulnerable;
  }

  public void savePersonaVulnerable(Context context) {
    String idUsuario = context.sessionAttribute("idUsuario");

    Map<String, List<String>> formParams = context.formParamMap();

    PersonaEnSituacionVulnerable personaEnSituacionVulnerable = crearPersonaVulnerable(formParams);

    IColaboradorService colaboradorService = ServiceLocator.colaboradorService();

    Colaborador colaborador = colaboradorService.traerColaboradorPorNombreDeUsuario(idUsuario);

    RegistroDePersonaVulnerable registroDePersonaVulnerable = new RegistroDePersonaVulnerable(personaEnSituacionVulnerable.getTarjeta(), personaEnSituacionVulnerable,colaborador);
    colaborador.agregarContribucion(registroDePersonaVulnerable);
    colaboradorService.guardarOActualizarColaborador(colaborador);

    context.header("HX-Redirect", "personaVulnerable/registroExitoso");
  }

  private FormularioRespondido registrarFormularioRespondido(Map<String, List<String>> formParams) {
    List<CampoRespondido> camposRespondidos = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : formParams.entrySet()) {
      String key = entry.getKey();
      List<String> values = entry.getValue();
      CampoFormulario campoFormulario = new CampoFormulario(false, key, values.get(1));
      CampoRespondido campoRespondido = new CampoRespondido(campoFormulario, values.get(0));
      camposRespondidos.add(campoRespondido);
    }

    return new FormularioRespondido(camposRespondidos);
  }

  private Direccion registrarDireccion(Map<String, List<String>> formParam) {
    String calle = formParam.get("calle").get(0);
    String altura = formParam.get("altura").get(0);
    String codigoPostal = formParam.get("codigoPostal").get(0);
    Direccion direccion;
    try {
      direccion = ObtenerCoordenadasAPI.getInstancia().obtenerDireccion(calle + " " + altura, codigoPostal);
    } catch (NoSuchElementException e) {
      throw new LocalidadInvalidaException("Revise la direccion ingresada");
    } catch (RuntimeException e) {
      throw new LocalidadInvalidaException("Tuvimos un problema en procesar su solicitud, por favor intenten mas tarde");
    }
    return direccion;
  }

  private List<PersonaEnSituacionVulnerable> registrarHijos(Map<String, List<String>> formParams, Direccion direccionPadre) {
    List<PersonaEnSituacionVulnerable> menoresACargo = new ArrayList<>();
    List<String> nombresHijos = formParams.remove("nombreHijo");
    List<String> apellidosHijos = formParams.remove("apellidoHijo");
    List<String> fechasNacimientoHijos = formParams.remove("fechaNacimientoHijo");
    List<String> tipoDocumentoHijos = formParams.remove("tipoDocumentoHijo");
    List<String> nroDocumentoHijos = formParams.remove("nroDocumentoHijo");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    if (nombresHijos == null) {
      return menoresACargo;
    }
    for (int i = 0; i < nombresHijos.size(); i++) {
      if (!esFechaAnterior(LocalDate.parse(fechasNacimientoHijos.get(i), formatter))) {
        throw new EdadInvalidaException("La fecha de nacimiento de los hijos no puede ser posterior a la fecha actual");
      }
      menoresACargo.add(new PersonaEnSituacionVulnerable(nombresHijos.get(i), apellidosHijos.get(i), LocalDate.parse(fechasNacimientoHijos.get(i), formatter), direccionPadre, TipoDocumento.valueOf(tipoDocumentoHijos.get(i)), nroDocumentoHijos.get(i)));
    }

    return menoresACargo;
  }

  public static boolean esFechaAnterior(LocalDate fechaAComparar) {
    return fechaAComparar.isBefore(LocalDate.now());
  }

  private List<String> tiposDocumento()
  {
    List<String> tiposDocumento = new ArrayList<>();

    for (TipoDocumento tipoDocumento : TipoDocumento.values()) {
      tiposDocumento.add(tipoDocumento.name());
    }

    return tiposDocumento;
  }

}
