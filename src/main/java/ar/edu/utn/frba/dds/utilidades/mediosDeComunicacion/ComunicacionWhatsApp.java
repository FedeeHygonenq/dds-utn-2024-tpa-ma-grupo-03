package ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import lombok.Data;

@Data
public class ComunicacionWhatsApp implements MedioDeComunicacion {

    private String token;
    private String accountSid;
    public ComunicacionWhatsApp(String token, String accSid){
        this.token = token;
        accountSid = accSid;
    }

    @Override
    public void enviar(Notificacion notificacion, String destinatario) {
        Twilio.init(accountSid, token);
        Message message = Message.creator(
            new com.twilio.type.PhoneNumber("whatsapp:"+destinatario),
            new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
            notificacion.mensaje).create();
    }
}
