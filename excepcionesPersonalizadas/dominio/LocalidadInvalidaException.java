package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class LocalidadInvalidaException extends RuntimeException{
    public LocalidadInvalidaException(String message) {
        super(message);
    }
}
