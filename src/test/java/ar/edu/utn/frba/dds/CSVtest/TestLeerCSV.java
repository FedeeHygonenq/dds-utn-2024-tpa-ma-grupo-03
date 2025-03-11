package ar.edu.utn.frba.dds.CSVtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ar.edu.utn.frba.dds.utilidades.CSV.CSVaInstancias;
import ar.edu.utn.frba.dds.utilidades.CSV.RepositorioColaboradoresMigrados;

import ar.edu.utn.frba.dds.services.imp.ColaboradorService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestLeerCSV {

  ColaboradorService colaboradorService = ServiceLocator.colaboradorService();
  CSVaInstancias csVaInstancias = CSVaInstancias.obtenerInstancia();

  @AfterEach
  public void tearDown() {
    colaboradorService.eliminarColaboradores();
  }

  @Test
  public void pruebaLeer() {
    assertDoesNotThrow(() -> csVaInstancias.transferencias("src/test/java/ar/edu/utn/frba/dds/CSVtest/prueba-csv-2.csv"));
    assertNotNull(colaboradorService.traerColaboradorPorDocumento("45111222"));
  }


}
