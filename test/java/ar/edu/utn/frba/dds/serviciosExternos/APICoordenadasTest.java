package ar.edu.utn.frba.dds.serviciosExternos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.services.externos.obtenerCoordenadas.ObtenerCoordenadasAPI;
import org.junit.jupiter.api.Test;

public class APICoordenadasTest {

  @Test
  public void testObtenerDireccion() {
    ObtenerCoordenadasAPI api = ObtenerCoordenadasAPI.getInstancia();
    Direccion coordenadas = api.obtenerDireccion("Av. Hipólito Yrigoyen 4600", "1824");
    assertEquals(coordenadas.getLatitud(), -34.7080444);
    assertEquals(coordenadas.getLongitud(), -58.3913046);
  }
}
