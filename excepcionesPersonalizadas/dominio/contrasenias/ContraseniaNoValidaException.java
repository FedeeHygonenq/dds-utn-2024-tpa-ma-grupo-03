package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.contrasenias;

public class ContraseniaNoValidaException extends RuntimeException{
    public ContraseniaNoValidaException(String mensaje){
        super(mensaje);
    }

}
