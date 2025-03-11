package ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.gestorPDF.FactoryParametrosPDF;
import ar.edu.utn.frba.dds.utilidades.gestorPDF.ParametrosPDF;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.RegistroApertura;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EstrategiaViandasColocadasRetiradas implements  EstrategiaReporte<Heladera>{
  public void generarReporte(String texto){
    ParametrosPDF parametros = FactoryParametrosPDF.crearParametrosPDF("pathPDFViandasPorHeladera", texto);
    ServiceLocator.gestorPDF().crearPDFConReporte(parametros);
  }

  public String armarReporte(List<Heladera> heladeras){
    StringBuilder texto = new StringBuilder();
    texto.append("Fecha de reporte: ").append(LocalDate.now().toString()).append("\n");
    for(Heladera heladera : heladeras){
      Map<MotivoApertura,Integer> contadoresApertura = contarViandas(heladera);
      texto.append(String.format("Heladera %s tuvo %d ingresos de viandas y %d retiros de vianda esta semana \n",
          heladera.getPuntoEstrategico().getNombre(),
          contadoresApertura.getOrDefault(MotivoApertura.INGRESO_VIANDA,0),
          contadoresApertura.getOrDefault(MotivoApertura.RETIRO_VIANDA,0)));
    }
    return texto.toString();
  }

  private Map<MotivoApertura, Integer> contarViandas(Heladera heladera){
    Map<MotivoApertura, Integer> contadoresApertura = new HashMap<MotivoApertura, Integer>();
    for(RegistroApertura apertura : heladera.getAperturas()){
      contadoresApertura.put(apertura.getMotivo(),
          contadoresApertura.getOrDefault(apertura.getMotivo(),0)+1);
    }
    return contadoresApertura;
  }


}
