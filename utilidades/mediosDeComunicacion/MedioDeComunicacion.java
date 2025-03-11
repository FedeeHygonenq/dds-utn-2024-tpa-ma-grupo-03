package ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion;

import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;



public interface MedioDeComunicacion {

    public void enviar(Notificacion notificacion, String destinatario);
}
