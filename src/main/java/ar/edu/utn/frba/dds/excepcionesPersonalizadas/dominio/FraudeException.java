package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio;

public class FraudeException extends RuntimeException{
  public FraudeException(String message) {
    super(message);
  }
}
