package ar.edu.utn.frba.dds.dominioGeneral.DTOs;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PuntoHeladeraDTOOut {

  private String nombre;
  private String latitud;
  private String longitud;
}