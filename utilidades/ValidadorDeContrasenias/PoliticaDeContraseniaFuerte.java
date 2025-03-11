package ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
@Getter
@Setter
public class PoliticaDeContraseniaFuerte implements PoliticaDeContrasenia {
  private final String pathAContraseniasDebiles ="src/main/resources/archivos/10k-most-common.txt";

  public PoliticaDeContraseniaFuerte() {
  }
  public Boolean cumplePolitica (String contrasenia){
    try (BufferedReader reader = new BufferedReader(new FileReader(pathAContraseniasDebiles))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.equals(contrasenia)) {
          return false;
        }
      }return true;
    } catch (IOException e) {
      e.printStackTrace(); // Manejamos cualquier excepción de E/S
    }
    return false;
  }

  public String errorContrasenia() { return "La contraseña insertada no es segura"; }

}
