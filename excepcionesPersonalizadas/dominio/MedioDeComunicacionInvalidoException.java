package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class MedioDeComunicacionInvalidoException extends RuntimeException{
  public MedioDeComunicacionInvalidoException(String mensaje){
    super(mensaje);
  }
}
