package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class ContribucionInvalidaException extends RuntimeException{
    public ContribucionInvalidaException(String mensaje){
        super(mensaje);
    }
}
