package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class UsuarioRepetidoException extends RuntimeException{
    public UsuarioRepetidoException(String mensaje){
        super(mensaje);
    }
}
