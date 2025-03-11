package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class FalloPeticionBaseDeDatosException extends RuntimeException {
    public FalloPeticionBaseDeDatosException(String message) {
        super(message);
    }
}
