package ar.edu.utn.frba.dds.utilidades.CSV;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

public class EnviarMail {

    private static EnviarMail instancia;
    private Resend resend;

    private EnviarMail() {
        // Constructor privado para evitar instanciación directa
        this.resend = new Resend("re_KxY4moht_8hZmcJRKHJ8SieVzTZaKxEb5");
    }

    public static EnviarMail obtenerInstancia() {
        if (instancia == null) {
            instancia = new EnviarMail();
        }

        return instancia;
    }
    public void enviar(Mail mail){
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(mail.getDestinatario())
                .subject(mail.getAsunto())
                .html(mail.getBody())
                .build();

        try {
            this.resend.emails().send(params);
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}
