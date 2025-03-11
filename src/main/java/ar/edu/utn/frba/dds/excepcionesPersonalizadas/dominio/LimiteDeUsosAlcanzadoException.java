package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class LimiteDeUsosAlcanzadoException extends RuntimeException {
    public LimiteDeUsosAlcanzadoException(String mensaje) {
        super(mensaje);
    }
}