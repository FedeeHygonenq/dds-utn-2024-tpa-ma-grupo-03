package ar.edu.utn.frba.dds.utilidades.notificador;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;

import java.util.List;

public class Notificador {
    public void enviar(List<Colaborador> colaboradores, Notificacion notificacion){
        for(Colaborador colaborador : colaboradores){
            colaborador.serNotificadoPor(notificacion);
        }
    }

    public void enviar(Tecnico tecnico, Notificacion notificacion){
        tecnico.serNotificadoPor(notificacion);
    }
}
