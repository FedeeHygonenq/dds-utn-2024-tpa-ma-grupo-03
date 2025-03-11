package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;
import ar.edu.utn.frba.dds.middlewares.AuthMiddleware;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.server.handlers.AppHandlers;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.JavalinRenderer;
import ar.edu.utn.frba.dds.utilidades.typeAdapter.HeladeraTypeAdapter;
import ar.edu.utn.frba.dds.utilidades.typeAdapter.LocalDateTimeTypeAdapter;
import ar.edu.utn.frba.dds.utilidades.typeAdapter.LocalDateTypeAdapter;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import ar.edu.utn.frba.dds.utilidades.typeAdapter.RegistroIncidenteTypeAdapter;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class Server {

    private static Javalin app = null;

    public static Javalin app() {
        if (app == null)
            throw new RuntimeException("App no inicializada");
        return app;
    }

    public static void init() {
        //Boolean testMode = LectorProperties.getBooleanPropertie("test_mode");
        Integer port = LectorProperties.getIntegerPropertie("server_port");
        HibernateUtil.initializeDatabase();
        ServiceLocator.heladeraService().inicializarSensoresHeladeras();

        app = Javalin.create(config()).start(port);
        AuthMiddleware.apply(app);
        AppHandlers.applyHandlers(app);
        Router.init(app);

        String testMode = System.getenv("test");
        boolean isTestMode = "true".equalsIgnoreCase(testMode);
        InicializadorDeDatos.init();
        if(isTestMode){
            InicializadorDeDatos.initDev();
        }
    /*
    if (testMode) {
      InicializadorDeDatos.initDev();
    }
    */

    }

    private static Consumer<JavalinConfig> config() {
        return config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
            });

            config.fileRenderer(new JavalinRenderer().register("hbs", (path, model, context) -> {
                Handlebars handlebars = new Handlebars();
                handlebars.setCharset(StandardCharsets.UTF_8);

                // Configuración de Gson con tu adaptador de LocalDateTime
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                        .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()) // Registra tu TypeAdapter
                        .registerTypeAdapter(Heladera.class, new HeladeraTypeAdapter())
                        .registerTypeAdapter(RegistroIncidente.class, new RegistroIncidenteTypeAdapter())
                        .create();

                handlebars.registerHelper("json", new Helper<Object>() {
                    @Override
                    public CharSequence apply(Object context, Options options) {
                        return gson.toJson(context); // Usa el Gson configurado
                    }
                });

                //Helper para obtener el nombre de un archivo
                handlebars.registerHelper("getFileName", new Helper<String>() {
                    @Override
                    public CharSequence apply(String filePath, Options options) {
                        if (filePath != null) {
                            return filePath.substring(filePath.lastIndexOf("/") + 1);
                        }
                        return "";
                    }
                });

                // Helper para comparar strings
                handlebars.registerHelper("ifEquals", new Helper<String>() {
                    @Override
                    public CharSequence apply(String arg1, Options options) throws IOException {
                        String arg2 = options.param(0);
                        if (arg1 != null && arg1.equals(arg2)) {
                            return options.fn(options.context);
                        } else {
                            return options.inverse(options.context);
                        }
                    }
                });
                Template template = null;
                try {
                    template = handlebars.compile(
                            "templates/" + path.replace(".hbs", ""));
                    context.contentType("text/html; charset=UTF-8");
                    return template.apply(model);
                } catch (IOException e) {
                    e.printStackTrace();
                    context.status(HttpStatus.NOT_FOUND);
                    return "No se encuentra la página indicada...";
                }
            }));
        };
    }

}
