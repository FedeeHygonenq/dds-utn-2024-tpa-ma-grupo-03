package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class CredencialesIncorrectasException extends RuntimeException {
    public CredencialesIncorrectasException(String message) {
        super(message);
    }
}
