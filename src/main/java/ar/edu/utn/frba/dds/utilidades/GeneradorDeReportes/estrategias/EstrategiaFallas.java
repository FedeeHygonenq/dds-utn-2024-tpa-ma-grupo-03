package ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias;

import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.gestorPDF.FactoryParametrosPDF;
import ar.edu.utn.frba.dds.utilidades.gestorPDF.ParametrosPDF;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EstrategiaFallas implements  EstrategiaReporte<RegistroIncidente>{
  public void generarReporte(String texto){
    ParametrosPDF parametros = FactoryParametrosPDF.crearParametrosPDF("pathPDFFallas", texto);
    ServiceLocator.gestorPDF().crearPDFConReporte(parametros);
  }

  public String armarReporte(List<RegistroIncidente> incidentes){
      StringBuilder texto = new StringBuilder();
      texto.append("Fecha de reporte: ").append(LocalDate.now().toString()).append("\n");
      Map<String,Integer> mapHeladeras = conteoFallosPorHeladera(incidentes);
      for(Map.Entry<String, Integer> entry : mapHeladeras.entrySet()){
        String linea = String.format("  Heladera %s tuvo %d cantidad de fallas o incidentes esta semana \n",
                                      entry.getKey(),entry.getValue());
        texto.append(linea);
      }
    return texto.toString();
  }

  private Map<String, Integer> conteoFallosPorHeladera(List<RegistroIncidente> incidentes){
    Map<String,Integer> mapHeladeras = new HashMap<>();
    for(RegistroIncidente incidente : incidentes){
      String nombrePunto = incidente.getHeladera().getPuntoEstrategico().getNombre();
        mapHeladeras.put(nombrePunto,mapHeladeras.getOrDefault(nombrePunto,0)+1);

    }
    return mapHeladeras;
  }
}
