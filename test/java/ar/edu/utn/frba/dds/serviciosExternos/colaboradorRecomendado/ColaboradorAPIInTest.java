package ar.edu.utn.frba.dds.serviciosExternos.colaboradorRecomendado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.Controllers.dtos.ColaboradorDTOOutAPI;
import ar.edu.utn.frba.dds.services.externos.colaboradorRecomendado.AdapterColaboradorRecomendado;
import ar.edu.utn.frba.dds.services.externos.colaboradorRecomendado.IAdapterColaboradorRecomendado;
import ar.edu.utn.frba.dds.services.externos.colaboradorRecomendado.ObtenerColaboradorRecomendado;
import ar.edu.utn.frba.dds.services.externos.colaboradorRecomendado.dtos.ColaboradorRecomendadoDTOIn;
import ar.edu.utn.frba.dds.services.externos.colaboradorRecomendado.dtos.WrapperColaboradoresRecomendadosDTOIn;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class ColaboradorAPIInTest {

  ObtenerColaboradorRecomendado obtenerColaboradorRecomendado = mock(ObtenerColaboradorRecomendado.class);

  ObjectMapper objectMapper = new ObjectMapper();

  List<ColaboradorRecomendadoDTOIn> colaboradores;

  @BeforeEach
  public void setUp() throws IOException {
    WrapperColaboradoresRecomendadosDTOIn colaboradoresWrapper = objectMapper.readValue(
        new File("src/test/resources/ejemplos/colaborador-API.json"), WrapperColaboradoresRecomendadosDTOIn.class
    );
    colaboradores = colaboradoresWrapper.getColaboradores();
  }

  @Test
  public void obtenerColaboradorRecomendadoTest(){
    when(obtenerColaboradorRecomendado.obtenerColaboradorRecomendado(100.0, 2, 2)).thenReturn(colaboradores);
    IAdapterColaboradorRecomendado adapterColaboradorRecomendado = new AdapterColaboradorRecomendado(obtenerColaboradorRecomendado);
    List<ColaboradorRecomendadoDTOIn> colaboradorDTOOutAPI = adapterColaboradorRecomendado.obtenerColaboradoresRecomendados(100.0, 2, 2);
    verify(obtenerColaboradorRecomendado).obtenerColaboradorRecomendado(100.0, 2, 2);
    assertEquals(colaboradores, colaboradorDTOOutAPI);
  }
}
