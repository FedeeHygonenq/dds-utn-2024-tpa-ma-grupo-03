package ar.edu.utn.frba.dds.utilidades.typeAdapter;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegistroIncidenteTypeAdapter extends TypeAdapter<RegistroIncidente> {
    private final Gson gson;

    public RegistroIncidenteTypeAdapter() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }

    @Override
    public void write(JsonWriter jsonWriter, RegistroIncidente registroIncidente) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(registroIncidente.getId());
        jsonWriter.name("fecha").jsonValue(gson.toJson(registroIncidente.getFecha()));
        jsonWriter.name("incidente").value(registroIncidente.getIncidente().toString());
        jsonWriter.name("heladera").jsonValue(gson.toJson(registroIncidente.getHeladera().getPuntoEstrategico().getNombre()));
        jsonWriter.name("descripcion").value(registroIncidente.getDescripcion());
        if (registroIncidente.getColaboradorQueReporto() != null) {
            jsonWriter.name("colaboradorQueReporto").value(registroIncidente.getColaboradorQueReporto().getNombre() + " " + registroIncidente.getColaboradorQueReporto().getApellido());
        } else {
            jsonWriter.name("colaboradorQueReporto").value("Sistema");
        }
        jsonWriter.name("pathFoto").value(registroIncidente.getPathFoto());
        jsonWriter.endObject();
    }

    @Override
    public RegistroIncidente read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
