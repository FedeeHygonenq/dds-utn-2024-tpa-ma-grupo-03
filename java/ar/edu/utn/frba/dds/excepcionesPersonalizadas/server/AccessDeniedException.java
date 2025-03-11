package ar.edu.utn.frba.dds.excepcionesPersonalizadas.server;

public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException() {
    super("Access Denied");
  }
}
