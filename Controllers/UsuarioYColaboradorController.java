package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.Controllers.dtos.ColaboradorDTOOutAPI;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.CampoFormulario;
import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.CampoRespondido;
import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.FormularioRespondido;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.*;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Contacto;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.*;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.contrasenias.ContraseniaNoValidaException;
import ar.edu.utn.frba.dds.server.roles.TipoRol;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import ar.edu.utn.frba.dds.services.IColaboradorService;
import ar.edu.utn.frba.dds.services.IUsuarioService;
import ar.edu.utn.frba.dds.services.externos.obtenerCoordenadas.ObtenerCoordenadasAPI;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.CSV.CSVaInstancias;
import ar.edu.utn.frba.dds.utilidades.CSV.ResultadoValidacion;
import ar.edu.utn.frba.dds.utilidades.GestorArchivos;
import ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias.ValidadorDeContrasenias;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import io.javalin.http.Context;

import io.javalin.http.UploadedFile;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UsuarioYColaboradorController implements ICrudViewsHandler {
  private IUsuarioService usuarioService;
  private IColaboradorService colaboradorService;

  public UsuarioYColaboradorController(IUsuarioService usuarioService, IColaboradorService colaboradorService) {
    this.usuarioService = usuarioService;
    this.colaboradorService = colaboradorService;
  }


  //RENDERS

  public void loginView(Context context) {
    Map<String, Object> model = new HashMap<>();

    if (context.queryParamMap().containsKey("redirect")) {
      String redirect = context.queryParam("redirect");
      model.put("redirect", redirect);
    }

    context.render("general/login.hbs", model);
  }

  public void cargarCSVView(Context context) {
    context.render("admin/cargaCSV.hbs");
  }

  public void mapaView(Context context) {
    List<Heladera> heladeras = ServiceLocator.heladeraService().traerHeladeras();
    Map<String, Object> model = new HashMap<>();
    model.put("heladeras", heladeras);
    context.render("general/mapa.hbs", model);
  }

  public void formularioColaboradorHumanoView(Context context) {
    Map<String, Object> model = new HashMap<>();
    List<String> tiposDocumento = tiposDocumento();


    model.put("tiposDocumento", tiposDocumento);

    context.render("colaboradorHumano/registroColabHumano.hbs", model);
  }

  public void formularioColaboradorJuridicoView(Context context) {
    Map<String, Object> model = new HashMap<>();
    List<String> tiposOrganizacion = tiposOrganizacion();
    List<String> tiposRubro = tiposRubro();


    model.put("tiposOrganizacion", tiposOrganizacion);
    model.put("tiposRubro", tiposRubro);


    context.render("colaboradorJuridico/registroColabJuridico.hbs", model);
  }

  public void preguntasFrecuentesView(Context context) {
    context.render("preguntasFrecuentes.hbs");
  }

  public void misContribucionesView(Context context) {
    //Completar
  }

  // RENDERS


  // FUNCIONES AUXILIARES

  private List<String> tiposOrganizacion() {
    List<String> tiposOrganizacion = new ArrayList<>();

    for (TipoOrganizacion tipoOrganizacion : TipoOrganizacion.values()) {
      tiposOrganizacion.add(tipoOrganizacion.name());
    }

    return tiposOrganizacion;
  }

  private List<String> tiposRubro() {
    List<String> tiposRubro = new ArrayList<>();

    for (TipoRubro tipoRubro : TipoRubro.values()) {
      tiposRubro.add(tipoRubro.name());
    }

    return tiposRubro;
  }

  private List<String> tiposDocumento() {
    List<String> tiposDocumento = new ArrayList<>();

    for (TipoDocumento tipoDocumento : TipoDocumento.values()) {
      tiposDocumento.add(tipoDocumento.name());
    }

    return tiposDocumento;
  }

  private static boolean esMayorDeEdad(LocalDate fechaNacimiento) {
    Period edad = Period.between(fechaNacimiento, LocalDate.now());
    return edad.getYears() >= 18; // Retorna true si tiene 18 años o más
  }

  private Direccion registrarDireccion(Map<String, List<String>> formParam) {
    String calle = formParam.get("calle").get(0);
    String altura = formParam.get("altura").get(0);
    String codigoPostal = formParam.get("codigoPostal").get(0);
    Direccion direccion;

    try {
      direccion = ObtenerCoordenadasAPI.getInstancia().obtenerDireccion(calle + " " + altura, codigoPostal);
    } catch (NoSuchElementException e) {
      throw new LocalidadInvalidaException("Revise la dirección ingresada.");
    } catch (RuntimeException e) {
      throw new LocalidadInvalidaException("Tuvimos un problema en procesar su solicitud. Por favor, intente más tarde.");
    }

    return direccion;
  }


  private Usuario registrarUsuario(Map<String, List<String>> formParams, TipoRol tipoRol) {
    String nombreUsuario = formParams.remove("usuario").get(0);
    String contrasenia = formParams.remove("contrasenia").get(0);

    if (this.usuarioService.nombreDeUsuarioExistente(nombreUsuario)) {
      throw new UsuarioRepetidoException("El nombre de usuario ingresado ya existe.");
    }

    ValidadorDeContrasenias validadorDeContrasenias = ValidadorDeContrasenias.desdeArchivoDeConfiguracion();

    if (!validadorDeContrasenias.cumplePoliticas(contrasenia)) {
      Integer minLength = LectorProperties.getIntegerPropertie("longitud", "src/main/resources/archivos/contrasenia.config");
      String eMessage = String.format("""
              La contraseña ingresada no cumple con las políticas de seguridad. Tené en cuenta que:
              La contraseña debe tener al menos %d caracteres.
              La contraseña no debe ser muy simple (ejemplo: 1234 o contraseña).""",
          minLength);
      throw new ContraseniaNoValidaException(eMessage);
    }

    contrasenia = usuarioService.hashearContrasenia(contrasenia);

    return new Usuario(nombreUsuario, contrasenia, tipoRol);
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

  private List<Contacto> registrarContactos(Map<String, List<String>> formParams) {
    List<Contacto> contactos = new ArrayList<>();
    String correoElectronico = formParams.remove("correoElectronico").get(0);

    if (!correoElectronico.isEmpty()) {
      contactos.add(new Contacto(ServiceLocator.comunicacionMail(), correoElectronico));
    }

    String nroWhatsapp = formParams.remove("nroWhatsapp").get(0);
    if (!nroWhatsapp.isEmpty()) {
      contactos.add(new Contacto(ServiceLocator.comunicacionWhatsApp(), nroWhatsapp));
    }
    String nroTelegram = formParams.remove("nroTelegram").get(0);

    if (!nroTelegram.isEmpty()) {
      contactos.add(new Contacto(ServiceLocator.comunicacionTelegram(), nroTelegram));
    }
    if (contactos.isEmpty()) {
      throw new MedioDeComunicacionInvalidoException("Debe ingresar al menos un medio de comunicación");
    }

    return contactos;
  }


  // MÉTODOS AUXILIARES

  public void realizarTransaccionDeOferta(String idUsuario, Oferta oferta) {
    this.colaboradorService.transaccionOfertaPorPuntos(idUsuario, oferta);
  }

  public void homeView(Context context) {
    String tipoRolStr = context.sessionAttribute("tipoRol");
    Map<String, Object> model = new HashMap<>();
    model.put("tipoUsuario", tipoRolStr);
    context.render("homeUsuarios.hbs", model);
  }

  public void asignarAtributosDeSesion(Context context, String idUsuario, Long idColaborador) {
    Usuario usuario = buscarUsuarioPorId(idUsuario);
    String tipoRolStr = usuario.getTipoRol().toString();

    context.sessionAttribute("authenticated", true);
    context.sessionAttribute("idUsuario", idUsuario);
    context.sessionAttribute("idColaborador", idColaborador);
    context.sessionAttribute("tipoRol", tipoRolStr);

    context.header("HX-Redirect", "/home");
  }

  //POST
  public void login(Context context) {
    String idUsuario = context.formParam("idUsuario");
    String contrasenia = context.formParam("contrasenia");

    Boolean usuarioYContraseniaCorrecto = usuarioService.credencialesCorrectas(idUsuario, contrasenia);

    if (usuarioYContraseniaCorrecto) {
      Colaborador colaborador = colaboradorService.traerColaboradorPorNombreDeUsuario(idUsuario);
      Long idColaborador = colaborador != null ? colaborador.getId() : null;
      asignarAtributosDeSesion(context, idUsuario, idColaborador);
    } else {
      throw new CredencialesIncorrectasException("Usuario o contraseña incorrectos.");//context modalerror.hbs ?
    }

    if (context.formParamMap().containsKey("redirect") && !context.formParam("redirect").isEmpty()) {
      context.header("HX-Redirect", context.formParam("redirect"));
    } else {
      context.header("HX-Redirect", "/home");
    }
  }


  public void cargarCSV(Context context) {
    UploadedFile archivoCSV = context.uploadedFile("archivoCSV");
    Map<String, Object> model = new HashMap<>();

    ResultadoValidacion resultadoValidacion = GestorArchivos.validarArchivoCSV(archivoCSV);

    if (!resultadoValidacion.isSuccess()) {
      // Si la validación falla, se muestra el mensaje de error.
      model.put("message", resultadoValidacion.getMessage());
      model.put("success", false);
      context.render("admin/resultadoDeCarga.hbs", model);

      return; // Terminar el procesamiento si hay un error en la validación.
    }
    try {
      String path = LectorProperties.getStringPropertie("csvPath") + archivoCSV.filename();
      GestorArchivos.subirArchivo(archivoCSV, path);
      CSVaInstancias.obtenerInstancia().transferencias(path);
    }

    catch (RuntimeException e) {
      model.put("message", e.getMessage());
      model.put("success", false);
      context.render("admin/resultadoDeCarga.hbs", model);
      return;
    }
    model.put("message", "¡Carga realizada con exito!");
    model.put("success", true);

    context.render("admin/resultadoDeCarga.hbs", model);
  }

  private Colaborador crearColaboradorHumano(Map<String, List<String>> formParams) {
    TipoColaborador tipoColaborador = TipoColaborador.HUMANO;

    Usuario usuario = this.registrarUsuario(formParams, TipoRol.COLABORADOR_HUMANO);
    String nombre = formParams.remove("nombre").get(0);
    String apellido = formParams.remove("apellido").get(0);

    String tipoDocumentoStr = formParams.remove("tipoDocumento").get(0);
    TipoDocumento tipoDocumento = TipoDocumento.valueOf(tipoDocumentoStr.toUpperCase());
    String nroDocumento = formParams.remove("nroDocumento").get(0);

    String fechaNacimientoStr = formParams.remove("fechaNacimiento").get(0);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);

    if (!esMayorDeEdad(fechaNacimiento)) {
      throw new EdadInvalidaException("El usuario debe ser mayor de edad para registrarse");
    }

    List<Contacto> contactos = this.registrarContactos(formParams);

    Direccion direccion = this.registrarDireccion(formParams);

    FormularioRespondido formularioRespondido = this.registrarFormularioRespondido(formParams);
    Tarjeta tarjeta = new Tarjeta(UUID.randomUUID().toString(), TipoPersona.Colaborador);
    return new Colaborador(usuario, tipoColaborador, tipoDocumento, nroDocumento, contactos, nombre, apellido, direccion, formularioRespondido, tarjeta);

  }


  private Colaborador crearColaboradorJuridico(Map<String, List<String>> formParams) {
    TipoColaborador tipoColaborador = TipoColaborador.JURIDICO;

    Usuario usuario = this.registrarUsuario(formParams, TipoRol.COLABORADOR_JURIDICO);
    String razonSocial = formParams.remove("razonSocial").get(0);
    String tipoOrganizacionStr = formParams.remove("tipoOrganizacion").get(0);
    TipoOrganizacion tipoOrganizacion = TipoOrganizacion.valueOf(tipoOrganizacionStr.toUpperCase());
    String tipoRubroStr = formParams.remove("tipoRubro").get(0);
    TipoRubro tipoRubro = TipoRubro.valueOf(tipoRubroStr.toUpperCase());

    List<Contacto> contactos = this.registrarContactos(formParams);
    Direccion direccion = this.registrarDireccion(formParams);

    FormularioRespondido formularioRespondido = this.registrarFormularioRespondido(formParams);

    return new Colaborador(usuario, tipoColaborador, tipoOrganizacion, contactos, direccion, tipoRubro, razonSocial, formularioRespondido);
  }

  public void saveColaboradorHumano(Context context) {
    Map<String, List<String>> formParams = context.formParamMap();

    Colaborador colaborador = crearColaboradorHumano(formParams);

    this.colaboradorService.guardarOActualizarColaborador(colaborador);

    this.notificarColaborador(colaborador);

    asignarAtributosDeSesion(context, colaborador.getUsuario().getUsuario(), colaborador.getId());
  }

  private void notificarColaborador(Colaborador colaborador) {
    StringBuilder texto = new StringBuilder();
    texto.append("El colaborador ").append(colaborador.getUsuario().getUsuario()).append(" ha sido registrado").append("\n")
            .append("Su usuario es: ").append(colaborador.getUsuario().getUsuario()).append("\n")
            .append("Y el numero de su tarjeta es: ").append(colaborador.tarjetaDeColaborador().getId());
    ServiceLocator.notificador().enviar(List.of(colaborador), new Notificacion(texto.toString(), "Registro exitoso"));
  }


  public void saveColaboradorJuridico(Context context) {
    Map<String, List<String>> formParams = context.formParamMap();

    Colaborador colaborador = crearColaboradorJuridico(formParams);

    this.colaboradorService.guardarOActualizarColaborador(colaborador);

    asignarAtributosDeSesion(context, colaborador.getUsuario().getUsuario(), colaborador.getId());

  }


  public void logout(Context context) {
    context.sessionAttribute("authenticated", null);
    context.sessionAttribute("idUsuario", null);
    context.sessionAttribute("tipoRol", null);
    context.redirect("/");
  }


  // POST


  public Colaborador buscarColaboradorPorNombreDeUsuario(String idUsuario) {
    return colaboradorService.traerColaboradorPorNombreDeUsuario(idUsuario);
  }

  public Usuario buscarUsuarioPorId(String idUsuario) {
    return usuarioService.traerPorId(idUsuario);
  }

  public void agregarColaborador(Colaborador colaborador) {
    colaboradorService.guardarOActualizarColaborador(colaborador);
  }

  public Colaborador buscarColaboradorPorId(Long id) {
    return colaboradorService.traerColaboradorPorId(id);
  }

  public Colaborador buscarColaboradorPorDocumento(String documento) {
    return colaboradorService.traerColaboradorPorDocumento(documento);
  }

  public List<ColaboradorDTOOutAPI> buscarColaboradoresParaServicioExterno() {
    return colaboradorService.buscarColaboradoresParaServicioExterno();
  }

  public List<Colaborador> buscarColaboradores() {
    return colaboradorService.traerColaboradores();
  }

  public void darDeBajaHeladeraView(Context context) {
    String idUsuario = context.sessionAttribute("idUsuario");

    Colaborador colaborador = colaboradorService.traerColaboradorPorNombreDeUsuario(idUsuario);

    List<Heladera> heladeras = colaboradorService.traerHeladerasDeColaboradorPorId(colaborador.getId());

    Map<String, Object> model = new HashMap<>();
    model.put("heladeras", heladeras);
    context.render("colaboradorJuridico/darDeBajaHeladera.hbs", model);
  }

  public void darDeBajaHeladera(Context context) {
    String idUsuario = context.sessionAttribute("idUsuario");

    Colaborador colaborador = colaboradorService.traerColaboradorPorNombreDeUsuario(idUsuario);

    String idHeladera = context.pathParam("id");
    Heladera heladera = ServiceLocator.heladeraService().traerHeladeraPorid(Long.parseLong(idHeladera));

    if (heladera != null) {
      heladera.setEstado(EstadoHeladera.DE_BAJA);
      ServiceLocator.heladeraService().agregarHeladera(heladera);
      context.header("HX-Redirect", "./reportefallas");
    } else {
      context.render("modals/modalError.hbs", Map.of("errorMessage", "Heladera no encontrada"));
    }
  }

  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
    context.render("general/eleccionTipoRegistro.hbs");
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
