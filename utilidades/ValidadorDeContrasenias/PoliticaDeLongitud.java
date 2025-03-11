package ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;

@Getter
@Setter
public class PoliticaDeLongitud implements PoliticaDeContrasenia {
  private Integer longitud;

  public PoliticaDeLongitud(Integer longitud) {
    this.longitud = longitud;
  }

  public PoliticaDeLongitud() {
    this.longitud = LectorProperties.getIntegerPropertie("longitud","src/main/resources/archivos/contrasenia.config");
  }
  public Boolean cumplePolitica(String contrasenia){
    return contrasenia.length() >= longitud;
  }

  public String errorContrasenia() { return "La contraseña debe tener más de " + this.longitud + " caracteres"; }
}
