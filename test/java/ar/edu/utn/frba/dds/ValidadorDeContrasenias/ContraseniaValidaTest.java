package ar.edu.utn.frba.dds.ValidadorDeContrasenias;

import java.io.FileNotFoundException;

import ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias.PoliticaDeContraseniaFuerte;
import ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias.PoliticaDeLongitud;
import ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias.ValidadorDeContrasenias;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class ContraseniaValidaTest {

  PoliticaDeContraseniaFuerte politicaDeContraseniaFuerte = new PoliticaDeContraseniaFuerte();
  PoliticaDeLongitud politicaDeLongitud = new PoliticaDeLongitud(8);

  ValidadorDeContrasenias passwordChecker = new ValidadorDeContrasenias(politicaDeLongitud,politicaDeContraseniaFuerte);
  ValidadorDeContrasenias passwordFromConfig = ValidadorDeContrasenias.desdeArchivoDeConfiguracion();

  @Test
  //La contraseña se encunetra en la lista de las peores contraseñas
  public void contraseniaEstaEnListaDePeores(){
    String passwordDeListaDePeores = "12345678";
    Assertions.assertFalse(politicaDeContraseniaFuerte.cumplePolitica(passwordDeListaDePeores));
  }
  @Test
  //La contraseña es demasiado corta
  public void contraseniaCorta(){
    String contrasenia = "mido5";
    Assertions.assertFalse(politicaDeLongitud.cumplePolitica(contrasenia));
  }

  @Test
  //La contraseña cumple todas las politicas
  public void contraseniaSegura(){
    String contrasenia = "SoyReDificilXD";
    Assertions.assertTrue(passwordChecker.cumplePoliticas(contrasenia));
    Assertions.assertTrue(passwordFromConfig.cumplePoliticas(contrasenia));
  }

}
