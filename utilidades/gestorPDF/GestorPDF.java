package ar.edu.utn.frba.dds.utilidades.gestorPDF;

import java.io.File;
import java.io.IOException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@NoArgsConstructor
@Data
public class GestorPDF {

  public void crearPDFConReporte(ParametrosPDF param){
    //Creating PDF document object
    PDDocument pdf = new PDDocument();
    agregarTexto(pdf, param);
    try {
      pdf.save(param.getFilePath());
      pdf.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void agregarTexto(PDDocument pdf, ParametrosPDF param){
    File file = new File(param.getFilePath());
    try {
      //Creando la pagina y agregandola
      PDPage pagina = new PDPage();
      pdf.addPage(pagina);
      PDPageContentStream contentStream = new PDPageContentStream(pdf, pagina);
      //Begin the Content stream
      contentStream.beginText();
      //Settenado la fuente
      contentStream.setFont(PDType1Font.TIMES_ROMAN, param.getFontSize());
      //Setteando la posicion del texto
      contentStream.newLineAtOffset(param.getOffsetX(), param.getOffsetY());
      // Dividiendo el texto en líneas para manejar saltos de línea
      String[] lines = param.getTexto().split("\n");
      for (String line : lines) {
        contentStream.showText(line);
        contentStream.newLineAtOffset(0,-30); // Nueva línea después de cada línea de texto
      }

      //Terminando el contentStream
      contentStream.endText();
      //Cerrando el contentStream
      contentStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void eliminarPDF(String pathPDF) {
    File filePDF = new File(pathPDF);

    // TODO manejar este if anidado y sus mensajes en eliminarPDF.
    if (filePDF.exists()) {
      if (filePDF.delete()) {
        System.out.println("Archivo PDF eliminado exitosamente.");
      } else {
        System.out.println("No se pudo eliminar el archivo PDF.");
      }
    } else {
      System.out.println("El archivo PDF no existe.");
    }
  }
}
