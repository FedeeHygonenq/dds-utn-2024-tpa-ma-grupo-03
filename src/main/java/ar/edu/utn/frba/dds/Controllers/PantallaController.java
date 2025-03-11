package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import ar.edu.utn.frba.dds.services.externos.obtenerCoordenadas.ObtenerCoordenadasAPI;
import ar.edu.utn.frba.dds.services.imp.HeladeraService;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.javalin.http.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class PantallaController implements ICrudViewsHandler {

    private HeladeraService heladeraService;

    public PantallaController(HeladeraService heladeraService){
        this.heladeraService = heladeraService;
    }

    public void obtenerCoordenadasParaMapa(Context context){
        Map<String, List<String>> formParams = context.queryParamMap();
        String direccionStr = formParams.get("direccion").get(0);

        Direccion  direccion = ObtenerCoordenadasAPI.getInstancia().obtenerDireccion(direccionStr);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("lat", direccion.getLatitud());
        jsonResponse.addProperty("lng", direccion.getLongitud());

        // Envía la respuesta JSON
        context.json(jsonResponse.toString());
    }

    public void reporteSemanalView(Context context){
        Map<String, Object> model = new HashMap<>();
        List<String> pdfsFallas = getPDFFiles("reportesFallas");
        List<String> pdfsViandasPorColaborador = getPDFFiles("reportesViandasPorColaborador");
        List<String> pdfsViandasPorHeladera = getPDFFiles("reportesViandasPorHeladera");
        model.put("pdfsFallas", pdfsFallas);
        model.put("pdfsViandasPorColaborador", pdfsViandasPorColaborador);
        model.put("pdfsViandasPorHeladera", pdfsViandasPorHeladera);
        context.render("admin/reportesSemanales.hbs",model);
    }

    private static List<String> getPDFFiles(String pdfType) {
        List<String> pdfFiles = new ArrayList<>();
        String folderPath = "src/main/resources/archivos/pdfs/" + pdfType; // Ruta basada en el tipo

        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".pdf")); // Filtra archivos PDF

        if (files != null) {
            for (File file : files) {
                // Genera URL relativa para cada PDF
                pdfFiles.add(folderPath+ "/" + file.getName());
            }
        }
        return pdfFiles;
    }
    @Override
    public void index(Context context) {
        List<PuntoHeladera> puntosHeladera = this.heladeraService.traerPuntosHeladera();
        Map<String, Object> model = new HashMap<>();
        model.put("locations", puntosHeladera);

        context.render("home.hbs", model);
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
