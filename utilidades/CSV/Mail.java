package ar.edu.utn.frba.dds.utilidades.CSV;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mail {
  private String destinatario;
  private String asunto;
  private String body;
}
