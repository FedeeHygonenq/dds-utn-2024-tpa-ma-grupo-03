package ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidadorDeContrasenias {

    private static List<PoliticaDeContrasenia> politicas;
    private static final String contraseniaConfig = "src/main/resources/archivos/contrasenia.config";

    // Constructor que toma al menos un argumento
    public ValidadorDeContrasenias(PoliticaDeContrasenia politica1) {
        this.politicas = new ArrayList<>();
        this.politicas.add(politica1);
    }

    // Constructor que toma un número variable de argumentos
    public ValidadorDeContrasenias(PoliticaDeContrasenia politica1, PoliticaDeContrasenia... restoDePoliticas) {
        this.politicas = new ArrayList<>();
        this.politicas.add(politica1);
        if (restoDePoliticas != null) {
            Collections.addAll(this.politicas, restoDePoliticas);

        }
    }

    public ValidadorDeContrasenias(List<PoliticaDeContrasenia> politicas) {
        this.politicas = politicas;
    }

    public ValidadorDeContrasenias() {

    }

    public static List<String> erroresEncontrados(String contrasenia) {
        List<String> errores = new ArrayList<>();

        for (PoliticaDeContrasenia politica : politicas)
        {
            if(!politica.cumplePolitica(contrasenia))
            {
                errores.add(politica.errorContrasenia());
            }
        }

        return errores;
    }

    public Boolean cumplePoliticas (String contrasenia){

        return this.politicas.stream().allMatch(politica->politica.cumplePolitica(contrasenia));
    }

    // Method para crear una instancia del validador desde un archivo de configuración
    public static ValidadorDeContrasenias desdeArchivoDeConfiguracion() {

        // Leer las políticas del archivo
        String politicasJson = LectorProperties.getStringPropertie("politicas",contraseniaConfig);

        ObjectMapper objectMapper = new ObjectMapper();
        List<PoliticasDeContrasenia> politicasArray = null;
        try {
            politicasArray = objectMapper.readValue(politicasJson, new TypeReference<List<PoliticasDeContrasenia>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<PoliticaDeContrasenia> politicas = new ArrayList<>();

        for (PoliticasDeContrasenia politicaNombre : politicasArray) {
            politicas.add(politicaNombre.getPolitica());
        }

        return new ValidadorDeContrasenias(politicas);
    }

}
