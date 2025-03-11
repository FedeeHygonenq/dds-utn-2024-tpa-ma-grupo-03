package ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion;

import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import lombok.Data;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Data
public class ComunicacionTelegram  implements MedioDeComunicacion, LongPollingSingleThreadUpdateConsumer {
    private String botToken;
    private TelegramClient telegramClient;
    public ComunicacionTelegram(String token){
        botToken = token;
        telegramClient = new OkHttpTelegramClient(token);
    }
    @Override
    public void enviar(Notificacion notificacion, String destinatario) {
    // We check if the update has a message and the message has tex
            SendMessage message = SendMessage // Create a message object
                .builder()
                .text(notificacion.getMensaje())
                .chatId(destinatario)
                .build();
            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            System.out.println(chat_id);

            SendMessage message = SendMessage // Create a message object
                .builder()
                .chatId(chat_id)
                .text(message_text)
                .build();
            try {
                telegramClient.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }



}
