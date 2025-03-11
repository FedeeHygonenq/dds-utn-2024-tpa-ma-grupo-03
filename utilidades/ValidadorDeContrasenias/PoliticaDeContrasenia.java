package ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias;

public interface PoliticaDeContrasenia {

  public Boolean cumplePolitica(String contrasenia);
  public String errorContrasenia();
}
