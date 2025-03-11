package ar.edu.utn.frba.dds.utilidades.typeAdapter;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class HeladeraTypeAdapter extends TypeAdapter<Heladera> {

  private final Gson gson;

  public HeladeraTypeAdapter() {
    this.gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
        .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
        .create();
  }

  @Override
  public void write(JsonWriter out, Heladera heladera) throws IOException {
    out.beginObject();
    out.name("id").value(heladera.getId());
    out.name("puntoEstrategico").jsonValue(gson.toJson(heladera.getPuntoEstrategico()));
    out.name("fechaFuncionamiento").value(heladera.getFechaFuncionamiento().toString());
    out.name("estado").value(heladera.getEstado().toString());
    out.name("modelo").jsonValue(gson.toJson(heladera.getModelo()));
    out.name("mesesActiva").value(heladera.getMesesActiva());
    out.name("viandas").jsonValue(gson.toJson(heladera.getViandas()));
    out.name("aperturas").jsonValue(gson.toJson(heladera.getAperturas()));
    out.name("reparacion").jsonValue(gson.toJson(heladera.getReparacion()));
    out.endObject();
  }

  @Override
  public Heladera read(JsonReader in) throws IOException {
    return null;
  }
}
