package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class EdadInvalidaException extends RuntimeException{
  public EdadInvalidaException(String mensaje){
    super(mensaje);
  }
}
