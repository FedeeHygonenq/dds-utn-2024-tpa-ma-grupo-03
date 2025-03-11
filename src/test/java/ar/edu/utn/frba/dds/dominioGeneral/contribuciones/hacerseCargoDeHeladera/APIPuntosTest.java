package ar.edu.utn.frba.dds.dominioGeneral.contribuciones.hacerseCargoDeHeladera;

import ar.edu.utn.frba.dds.services.externos.puntoRecomendado.AdapterPuntosRecomendados;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class APIPuntosTest {

  AdapterPuntosRecomendados pruebaAPI = new AdapterPuntosRecomendados();
  Area area = Area.builder().puntoCentral(Direccion.builder().latitud(10.0).longitud(10.0).build()).kmALaRedonda(10).build();
  PuntoHeladera puntoOriginal = PuntoHeladera.builder().nombre("UTN CAMPUS").direccion(Direccion.builder().longitud(-58.46770039086419).latitud(-34.65948784981445).build()).build();

  List<PuntoHeladera> lista = new ArrayList<>();
    public Boolean compararPuntos(PuntoHeladera punto1,PuntoHeladera punto2){
      if(Objects.equals(punto1.getNombre(), punto2.getNombre()) && Objects.equals(punto1.getDireccion().getLongitud(), punto2.getDireccion().getLongitud())
      && Objects.equals(punto1.getDireccion().getLatitud(), punto2.getDireccion().getLatitud()))
      {
        return true;
      }
      return false;

    }
  @Test
  public void obtenerPuntoYComparar() {


      lista = pruebaAPI.obtenerPuntos(area);



    PuntoHeladera puntoDeAPI = lista.get(0);
    Assertions.assertEquals(puntoOriginal, puntoDeAPI);
  }

}
