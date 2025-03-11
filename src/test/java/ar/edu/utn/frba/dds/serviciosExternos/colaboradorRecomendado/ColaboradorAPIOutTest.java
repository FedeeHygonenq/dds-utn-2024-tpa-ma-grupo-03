package ar.edu.utn.frba.dds.serviciosExternos.colaboradorRecomendado;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.Controllers.dtos.ColaboradorDTOOutAPI;
import ar.edu.utn.frba.dds.services.imp.ColaboradorService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ColaboradorAPIOutTest {

  List<Colaborador> colaboradores;
  List<ColaboradorDTOOutAPI> colaboradoresDTOOutAPIS;


  @BeforeEach
  void setUp() {
    colaboradores = ColaboradorBuilder.generarColaboradoresParaTest();
    colaboradoresDTOOutAPIS = ColaboradorBuilder.generarColaboradoresDTOOUTParaTest();
  }

  @AfterEach
    void tearDown() {

    }

  @Test
  void mapeoCorrectoDeController(){
    ColaboradorService colaboradorService =  ServiceLocator.colaboradorService();
    colaboradores.forEach(colaboradorService::guardarOActualizarColaborador);
    List<ColaboradorDTOOutAPI> colaboradoresAPIFinal = colaboradorService.buscarColaboradoresParaServicioExterno();
    assertEquals(colaboradoresDTOOutAPIS, colaboradoresAPIFinal);
  }


}
