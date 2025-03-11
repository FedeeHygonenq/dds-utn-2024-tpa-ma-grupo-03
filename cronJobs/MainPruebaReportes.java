package ar.edu.utn.frba.dds.cronJobs;

import ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias.EstrategiaFallas;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;

import ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.GeneradorDeReportes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainPruebaReportes {
  public static void main(String[] s){


    List<RegistroIncidente> incidentes = new ArrayList<>();
    PuntoHeladera puntoHeladera = PuntoHeladera.builder().nombre("Pepito").build();
    PuntoHeladera puntoHeladera2 = PuntoHeladera.builder().nombre("Pepon").build();
    Heladera heladera = Heladera.builder().estado(EstadoHeladera.ACTIVA).puntoEstrategico(puntoHeladera).build();
    RegistroIncidente registro1 = RegistroIncidente.builder().incidente(TipoIncidente.TEMPERATURA).fecha(LocalDateTime.now()).heladera(heladera).build();
    Heladera heladera2 = Heladera.builder().estado(EstadoHeladera.ACTIVA).puntoEstrategico(puntoHeladera2).build();
    RegistroIncidente registro2 = RegistroIncidente.builder().incidente(TipoIncidente.TEMPERATURA).fecha(LocalDateTime.now()).heladera(heladera2).build();
    incidentes.add(registro1);
    incidentes.add(registro1);
    incidentes.add(registro2);
    GeneradorDeReportes generadorDeReportes = GeneradorDeReportes.builder().estrategia(new EstrategiaFallas()).build();
    generadorDeReportes.reportar(incidentes);

  }
}
