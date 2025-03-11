package ar.edu.utn.frba.dds.CSVtest;

import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion.ComunicacionMail;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import org.junit.jupiter.api.Test;

public class testCorreo {
  private static com.resend.services.emails.model.CreateEmailOptions CreateEmailOptions;

  @Test
  public void pruebaMail() {
    Integer documento = 12345;
    ComunicacionMail mailSender = ServiceLocator.comunicacionMail();
    Notificacion notificacion = new Notificacion("<strong>Sus credenciales de acceso son las siguientes:</strong><br>" +
        "Usuario: " + documento.toString() + "<br>" +
        "Contraseña: " + documento.toString(), "Usuario y contraseña en nuestro nuevo sistema!");
    mailSender.enviar(notificacion, "testsdds2024@gmail.com");


  }
}

