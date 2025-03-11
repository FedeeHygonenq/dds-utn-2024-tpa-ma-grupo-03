package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.Controllers.dtos.ColaboradorDTOOutAPI;
import ar.edu.utn.frba.dds.server.roles.TipoRol;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Router {
    public static void init(Javalin app) {
        app.get("/prueba", ctx -> ctx.result("Hola mundo!"));

        //Ejemplo Query Params
        app.get("/saludo", ctx -> {
            ctx.result("Hola " + ctx.queryParam("nombre") + " " + ctx.queryParam("apellido"));
        });

        app.get("/simeal/colaboradores", ctx -> {
            ctx.json(ServiceLocator.usuarioYColaboradorController().buscarColaboradoresParaServicioExterno());
        });

        //Pagina inicial
        app.get("/", ServiceLocator.pantallaController()::index);
        // Autenticación
        app.get(LectorProperties.getStringPropertie("rutaLogin"), ServiceLocator.usuarioYColaboradorController()::loginView); // Muestra pantalla de login
        app.post(LectorProperties.getStringPropertie("rutaLogin"), ServiceLocator.usuarioYColaboradorController()::login);    // Verifica el login

        app.get(LectorProperties.getStringPropertie("rutaAccesoDenegado"), context -> {
            Map<String, Object> model = new HashMap<>();
            if (context.queryParamMap().containsKey("redirect")) {
                String redirect = "?redirect=" + context.queryParam("redirect");
                model.put("redirect", redirect);
            }
            context.render("general/accesoDenegado.hbs", model);
        });

        // Registro de usuarios y colaboradores
        app.get(LectorProperties.getStringPropertie("rutaSignup"), ServiceLocator.usuarioYColaboradorController()::create); // Chequeado - 19/10/2024
        app.get(LectorProperties.getStringPropertie("rutaSignupColabHumano"), ServiceLocator.usuarioYColaboradorController()::formularioColaboradorHumanoView); // Chequeado - 19/10/2024
        app.get(LectorProperties.getStringPropertie("rutaSignupColabJuridico"), ServiceLocator.usuarioYColaboradorController()::formularioColaboradorJuridicoView); // Chequeado - 19/10/2024
        app.post(LectorProperties.getStringPropertie("rutaSignupColabHumano"), ServiceLocator.usuarioYColaboradorController()::saveColaboradorHumano); // Chequeado - 19/10/2024
        app.post(LectorProperties.getStringPropertie("rutaSignupColabJuridico"), ServiceLocator.usuarioYColaboradorController()::saveColaboradorJuridico); // Chequeado - 19/10/2024
        app.get(LectorProperties.getStringPropertie("rutaSignupPersonaVulnerable"), ServiceLocator.personaVulnerableController()::formularioPersonaVulnerableView); // Chequeado - 19/10/2024
        app.post(LectorProperties.getStringPropertie("rutaSignupPersonaVulnerable"), ServiceLocator.personaVulnerableController()::savePersonaVulnerable); // Chequeado - 19/10/2024
        app.get(LectorProperties.getStringPropertie("rutaPersonaVulnerableRegistroExitoso"), ServiceLocator.personaVulnerableController()::registroExitosoView, TipoRol.COLABORADOR_HUMANO); // Chequeado - 19/10/2024
        // Home
        app.get(LectorProperties.getStringPropertie("rutaHome"), ServiceLocator.usuarioYColaboradorController()::homeView, TipoRol.TECNICO, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO, TipoRol.ADMINISTRADOR); // Chequeado - 19/10/2024

        // Preguntas Frecuentes
        app.get(LectorProperties.getStringPropertie("rutaPreguntasFrecuentes"), ServiceLocator.usuarioYColaboradorController()::preguntasFrecuentesView);

        // Catálogo
        app.get(LectorProperties.getStringPropertie("rutaCatalogo"), ServiceLocator.catalogoController()::index, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO); //Chequeado - 19/10/2024
        app.post(LectorProperties.getStringPropertie("rutaComprar"), ServiceLocator.catalogoController()::canjear, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO); //Chequeado - 19/10/2024
        app.get(LectorProperties.getStringPropertie("rutaCanjes"), ServiceLocator.catalogoController()::canjesView, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO);
        app.post(LectorProperties.getStringPropertie("rutaLogout"), ServiceLocator.usuarioYColaboradorController()::logout); // Chequeado - 19/10/2024

        // Cargar CSV
        app.get(LectorProperties.getStringPropertie("rutaCargarCSV"), ServiceLocator.usuarioYColaboradorController()::cargarCSVView, TipoRol.ADMINISTRADOR); // Chequeado - 21/10/2024
        app.post(LectorProperties.getStringPropertie("rutaCargarCSV"), ServiceLocator.usuarioYColaboradorController()::cargarCSV, TipoRol.ADMINISTRADOR); // REVISAR tema csv

        // Mapa y coordenadas
        app.get(LectorProperties.getStringPropertie("rutaMapa"), ServiceLocator.usuarioYColaboradorController()::mapaView, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO, TipoRol.ADMINISTRADOR, TipoRol.TECNICO);
        app.get(LectorProperties.getStringPropertie("rutaCoordenadasMapa"), ServiceLocator.pantallaController()::obtenerCoordenadasParaMapa);

        // Suscripción
        app.get(LectorProperties.getStringPropertie("rutaSuscribirHeladera"), ServiceLocator.suscripcionController()::suscribirseAHeladerasView, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO); //Chequeado - 22/10/24 (no saque la prueba)
        app.post(LectorProperties.getStringPropertie("rutaSuscribirHeladera"), ServiceLocator.suscripcionController()::suscribirseAHeladeras, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO); //Chequeado - 22/10/24

        // Contribuciones
        app.get(LectorProperties.getStringPropertie("rutaContribuir"), ServiceLocator.contribucionController()::contribucionView, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO); //Chequeado - 22/10/24 (no saque la prueba)
        app.post(LectorProperties.getStringPropertie("rutaContribuir"), ServiceLocator.contribucionController()::contribuir);
        app.get(LectorProperties.getStringPropertie("rutaConsultarPuntoRecomendado"), ServiceLocator.contribucionController()::consultarPuntoRecomendado);
        //Si cambian el app.get de abajo, pongan su ruta en el properties.
        //app.get("/colaborador/misContribuciones", ServiceLocator.UsuarioYColaboradorController()::misContribucionesView, TipoRol.COLABORADOR_HUMANO, TipoRol.COLABORADOR_JURIDICO)

        // Reportes fallas
        app.get(LectorProperties.getStringPropertie("rutaReporteFallas"), ServiceLocator.incidenteController()::reporteDeFallaView, TipoRol.ADMINISTRADOR, TipoRol.COLABORADOR_HUMANO, TipoRol.TECNICO);
        app.post(LectorProperties.getStringPropertie("rutaReporteFallasNuevo"), ServiceLocator.incidenteController()::reporteDeFallaNuevo, TipoRol.ADMINISTRADOR, TipoRol.COLABORADOR_HUMANO, TipoRol.TECNICO);
        app.post(LectorProperties.getStringPropertie("rutaReporteTecnico"), ServiceLocator.tecnicoController()::reporteTecnico, TipoRol.TECNICO);

        app.get(LectorProperties.getStringPropertie("rutaReporteSemanal"), ServiceLocator.pantallaController()::reporteSemanalView, TipoRol.ADMINISTRADOR, TipoRol.TECNICO);

        //Mis heladeras
        app.get(LectorProperties.getStringPropertie("rutaAccionHeladera"), ServiceLocator.heladeraController()::accionSobreHeladeraView, TipoRol.COLABORADOR_JURIDICO);
        app.get(LectorProperties.getStringPropertie("rutaAccionHeladeraBaja"), ServiceLocator.heladeraController()::darDeBajaHeladeraView, TipoRol.COLABORADOR_JURIDICO);
        app.post(LectorProperties.getStringPropertie("rutaAccionHeladeraBaja"), ServiceLocator.heladeraController()::darDeBajaHeladera, TipoRol.COLABORADOR_JURIDICO);
        app.get(LectorProperties.getStringPropertie("rutaAccionHeladeraConsultas"), ServiceLocator.heladeraController()::consultaHeladerasView, TipoRol.COLABORADOR_JURIDICO);
        app.post(LectorProperties.getStringPropertie("rutaAccionHeladeraConsultas"), ServiceLocator.heladeraController()::consultaHeladeras, TipoRol.COLABORADOR_JURIDICO);


        app.get(LectorProperties.getStringPropertie("rutaSolicitarApertura"), ServiceLocator.heladeraController()::solicitarAperturaView, TipoRol.COLABORADOR_HUMANO);
        app.post(LectorProperties.getStringPropertie("rutaSolicitarApertura"), ServiceLocator.heladeraController()::solicitarApertura, TipoRol.COLABORADOR_HUMANO);


        app.get(LectorProperties.getStringPropertie("rutaArchivos"), Router::handleFileRequest);

    }

    private static void handleFileRequest(Context ctx) throws IOException {

        String path = ctx.path();
        String filefPath = path.substring(1);
        // Construye la ruta absoluta combinando la base y la ruta capturada
        Path filePath = Path.of(filefPath).normalize();

        // Verifica si el archivo existe y lo sirve
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            ctx.contentType(Files.probeContentType(filePath)); // Detecta el tipo MIME
            ctx.result(Files.newInputStream(filePath));       // Sirve el archivo
        } else {
            ctx.status(404).result("Archivo no encontrado");
        }

    }

}
