package ar.edu.utn.frba.dds.TestsDeReportes;

import static ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente.FRAUDE;
import static ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente.TEMPERATURA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias.EstrategiaFallas;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EstrategiasReporteTest {

  @Test
  public void estrategiaFallasTest(){
    Heladera mockHeladeraCampus = mock(Heladera.class);
    Heladera mockHeladeraMedrano = mock(Heladera.class);
    PuntoHeladera mockPuntoCampus = mock(PuntoHeladera.class);
    PuntoHeladera mockPuntoMedrano = mock(PuntoHeladera.class);

    RegistroIncidente registroCampus1 = RegistroIncidente.builder().heladera(mockHeladeraCampus).incidente(TEMPERATURA).build();
    RegistroIncidente registroCampus2 = RegistroIncidente.builder().heladera(mockHeladeraCampus).incidente(FRAUDE).build();
    RegistroIncidente registroMedrano = RegistroIncidente.builder().heladera(mockHeladeraMedrano).incidente(FRAUDE).build();
    List<RegistroIncidente> registros = new ArrayList<>();
    registros.add(registroCampus1);
    registros.add(registroCampus2);
    registros.add(registroMedrano);
    // Configurar los mocks para devolver los valores esperados
    when(mockHeladeraCampus.getPuntoEstrategico()).thenReturn(mockPuntoCampus);
    when(mockHeladeraMedrano.getPuntoEstrategico()).thenReturn(mockPuntoMedrano);
    when(mockPuntoCampus.getNombre()).thenReturn("UTN Campus");
    when(mockPuntoMedrano.getNombre()).thenReturn("UTN Medrano");

    EstrategiaFallas estrategiaFallas = new EstrategiaFallas();
    String reporteGenerado = estrategiaFallas.armarReporte(registros);
    StringBuilder reporteEsperado = new StringBuilder();
    reporteEsperado.append("Fecha de reporte: ").append(LocalDate.now().toString()).append("\n");
    reporteEsperado.append("  Heladera UTN Campus tuvo 2 cantidad de fallas o incidentes esta semana \n");
    reporteEsperado.append("  Heladera UTN Medrano tuvo 1 cantidad de fallas o incidentes esta semana \n");
    Assertions.assertEquals(reporteGenerado,reporteEsperado.toString());

  }
}
