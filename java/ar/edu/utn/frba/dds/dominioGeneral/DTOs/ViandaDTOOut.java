package ar.edu.utn.frba.dds.dominioGeneral.DTOs;

import java.time.LocalDate;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViandaDTOOut {

  private String comida;
  private LocalDate fechaDeCaducidad;
  private Integer calorias;
  private Double peso;
  private Boolean estaEntregada;


}
