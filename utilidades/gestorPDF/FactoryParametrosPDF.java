package ar.edu.utn.frba.dds.utilidades.gestorPDF;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import java.time.LocalDate;

public class FactoryParametrosPDF {
  public static ParametrosPDF crearParametrosPDF(String clave, String texto){
      StringBuilder pathRegistro = new StringBuilder();
      pathRegistro.append(LectorProperties.getStringPropertie(clave));
      switch (clave){
        case "pathPDFFallas":
          pathRegistro.append("/RegistroFallas-").append(LocalDate.now()).append(".pdf");
          break;
        case "pathPDFViandasPorColaborador" :
          pathRegistro.append("/RegistroViandasPorColaborador-").append(LocalDate.now()).append(".pdf");
          break;
        case"pathPDFViandasPorHeladera":
          pathRegistro.append("/RegistroViandasColocadasRetiradas-").append(LocalDate.now()).append(".pdf");
          break;
        case "pathPDFTest":
          pathRegistro.append("/Test").append(".pdf");
          break;
        default:
          throw new RuntimeException("Invalida clave");
      }

        return ParametrosPDF.builder().texto(texto).
        filePath(pathRegistro.toString()).
        offsetX(LectorProperties.getIntegerPropertie("offsetX")).
        offsetY(LectorProperties.getIntegerPropertie("offsetY")).
        fontSize(LectorProperties.getIntegerPropertie("fontSize")).build();


  }
}
