package ar.edu.utn.frba.dds.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.junit.jupiter.api.Test;

public class UsuarioServiceTest {

  private IUsuarioService usuarioService = ServiceLocator.usuarioService();
  @Test
  public void hasheoContraseniaTest(){
    String contrasenia = "Hola12345$";

    String contraseniaHasheada = usuarioService.hashearContrasenia(contrasenia);

    assertTrue(BCrypt.verifyer().verify(contrasenia.toCharArray(), contraseniaHasheada.toCharArray()).verified);
  }
}
