package ar.edu.utn.frba.dds.excepcionesPersonalizadas.suscripciones;

public class YaEstaSuscritoException extends RuntimeException{

    public YaEstaSuscritoException(String message) {
        super(message);
    }
}
