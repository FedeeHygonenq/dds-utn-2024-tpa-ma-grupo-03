package ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias;

import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.gestorPDF.FactoryParametrosPDF;
import ar.edu.utn.frba.dds.utilidades.gestorPDF.ParametrosPDF;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import java.time.LocalDate;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EstrategiasViandasColab implements EstrategiaReporte<Colaborador>{

  public void generarReporte(String texto){
    ParametrosPDF parametrosPDF = FactoryParametrosPDF.crearParametrosPDF("pathPDFViandasPorColaborador", texto);
    ServiceLocator.gestorPDF().crearPDFConReporte(parametrosPDF);
  }

  public String armarReporte(List<Colaborador> colaboradores){
    StringBuilder texto = new StringBuilder();
    texto.append("Fecha de reporte: ").append(LocalDate.now().toString()).append("\n");
    for(Colaborador colaborador : colaboradores){
      texto.append(String.format("El colaborador %s dono %d viandas esta semana \n",
          colaborador.getUsuario().getUsuario(), colaborador.viandasDonadas()));
    }
    return texto.toString();
  }
}
