package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class PuntosNoSuficientesException extends RuntimeException {
    public PuntosNoSuficientesException(String message) {
        super(message);
    }
}
