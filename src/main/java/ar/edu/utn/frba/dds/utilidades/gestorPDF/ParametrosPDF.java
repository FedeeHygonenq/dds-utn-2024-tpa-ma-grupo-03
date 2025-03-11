package ar.edu.utn.frba.dds.utilidades.gestorPDF;

import lombok.Builder;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDDocument;

@Data
@Builder
public class ParametrosPDF {
  private String filePath;
  private Integer offsetX;
  private Integer offsetY;
  private Integer fontSize;
  private String texto;

}
