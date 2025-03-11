package ar.edu.utn.frba.dds.TestsDeReportes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.gestorPDF.FactoryParametrosPDF;
import org.junit.jupiter.api.Test;

public class TestPDF {

  @Test
  //Creamos un pdf
  public void crearPDFTest(){
    assertDoesNotThrow(()-> ServiceLocator.gestorPDF().
        crearPDFConReporte(FactoryParametrosPDF.crearParametrosPDF("pathPDFTest", "Soy un Test")));
  }

}
