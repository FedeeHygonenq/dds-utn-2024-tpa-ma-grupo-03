package ar.edu.utn.frba.dds.utilidades.CSV;

import ar.edu.utn.frba.dds.Controllers.UsuarioYColaboradorController;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.*;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoDocumento;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class CSVaInstancias {
  private static CSVaInstancias instancia;
  private RepositorioColaboradoresMigrados repositorio;

  public CSVaInstancias() {
    this.repositorio = new RepositorioColaboradoresMigrados();
  }

  public void transferencias(String csvFile) {
    UsuarioYColaboradorController usuarioYColaboradorController = ServiceLocator.usuarioYColaboradorController();

    char separator = detectSeparator(csvFile);

    try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile))
        .withCSVParser(new CSVParserBuilder().withSeparator(separator).build())
        .build()) {
      String[] nextLine = reader.readNext();
      nextLine = reader.readNext();

      while (nextLine != null) {
        this.generarColaborador(nextLine, usuarioYColaboradorController);

        nextLine = reader.readNext();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      throw new RuntimeException(e);
    }
  }

  private void generarColaborador(String[] nextLine, UsuarioYColaboradorController usuarioYColaboradorController) {
    TipoDocumento tipoDocumento = TipoDocumento.valueOf(nextLine[0]);
    String documento = nextLine[1];
    String nombre = nextLine[2];
    String apellido = nextLine[3];
    String mail = nextLine[4];
    LocalDate fechaColaboracion = LocalDate.parse(nextLine[5], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    String formaColaboracion = nextLine[6];
    Integer cantidad = Integer.valueOf(nextLine[7]);

    GenerarContribucion generarContribucion = new GenerarContribucion();
    GenerarColaborador generarColaborador = new GenerarColaborador();
    Colaborador colaborador = usuarioYColaboradorController.buscarColaboradorPorDocumento(documento);
    if (colaborador != null) {
      Contribucion contribucion = generarContribucion.generar(fechaColaboracion, formaColaboracion, colaborador, cantidad);
      colaborador.agregarContribucion(contribucion);
    } else {
      colaborador = generarColaborador.generar(tipoDocumento, documento, nombre, apellido, mail);
      Contribucion contribucion = generarContribucion.generar(fechaColaboracion, formaColaboracion, colaborador, cantidad);
      colaborador.agregarContribucion(contribucion);

      this.enviarMail(documento, mail);
    }
  usuarioYColaboradorController.agregarColaborador(colaborador);
  }

  private void enviarMail(String documento, String mail) {
    String bodyMail = ("<strong>Sus credenciales de acceso son las siguientes:</strong><br>" +
        "Usuario: " + documento + "<br>" +
        "Contraseña: " + documento);
    String subjectMail = "Usuario y contraseña en nuestro nuevo sistema!";
    EnviarMail envioDeMail = EnviarMail.obtenerInstancia();
    envioDeMail.enviar(Mail.builder().destinatario(mail).asunto(subjectMail).body(bodyMail).build());
  }

  // Method para detectar el separador (coma o punto y coma) en la primera línea del archivo CSV
  private char detectSeparator(String csvFile) {
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      String line = br.readLine();
      if (line != null) {
        if (line.contains(";")) {
          return ';';
        } else if (line.contains(",")) {
          return ',';
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Valor predeterminado si no se encuentra ningún separador específico
    return ',';
  }

  public static CSVaInstancias obtenerInstancia() {
    if (instancia == null) {
      instancia = new CSVaInstancias();
    }
    return instancia;
  }
}
