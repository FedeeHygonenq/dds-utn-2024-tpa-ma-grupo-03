package ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.Data;

@Data

public class ComunicacionMail implements MedioDeComunicacion {

  private Resend resend;

  public ComunicacionMail() {
    this.resend = new Resend(LectorProperties.getStringPropertie("tokenMail"));
  }

  @Override
  public void enviar(Notificacion notificacion, String destinatario) {
    CreateEmailOptions params = CreateEmailOptions.builder()
        .from("onboarding@resend.dev")
        .to(destinatario)
        .subject(notificacion.getAsunto())
        .html(notificacion.getMensaje())
        .build();

    try {
      this.resend.emails().send(params);
    } catch (ResendException e) {
      e.printStackTrace();
    }
  }
}
