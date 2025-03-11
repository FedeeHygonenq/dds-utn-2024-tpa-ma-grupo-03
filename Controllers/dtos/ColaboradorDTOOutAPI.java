package ar.edu.utn.frba.dds.Controllers.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@JsonAutoDetect
@Builder
public class ColaboradorDTOOutAPI {
  private String nombre;
  private String apellido;
  private String contacto;
  private Double puntos;
  @JsonProperty(value = "cantDonaciones")
  private Integer cantidadDeColaboraciones;
}
