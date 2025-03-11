package ar.edu.utn.frba.dds.dominioGeneral.suscripciones;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;

import java.util.List;

public interface Suscripcion {


    public void notificar(Heladera heladera, List<Heladera> heladerasCercanas);
    public void registrarMensaje(String mensaje);
    public void agregar(Colaborador colaborador);
    public void eliminar(Colaborador colaborador);

    Boolean estaSuscrito(Colaborador colaborador);
    
}
