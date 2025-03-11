package ar.edu.utn.frba.dds.dominioGeneral.DTOs;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoDocumento;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoOrganizacion;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoRubro;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Contacto;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos.BilleteraDePuntos;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorDTOOut {

  private String nombre;
  private String apellido;
  private TipoColaborador tipoColaborador;
  private TipoOrganizacion tipoOrganizacion;
  private TipoDocumento tipoDocumento;
  private String numeroDeDocumento;
  private Direccion direccion; // DTO de Dirección si fuera necesario.
  private TipoRubro rubro;
  private String razonSocial;
  private List<Contacto> mediosDeComunicacion; // Pueden incluirse solo los datos relevantes, como tipo de contacto y valor.
  private BilleteraDePuntos billeteraDePuntos; // O solo el total de puntos, según sea necesario.
  private List<Tarjeta> tarjetas; // Si es necesario mostrar las tarjetas asociadas.
  private LocalDate fechaDeRegistro;
  private Integer viandasDonadas; // Total de viandas donadas, calculado a partir de las contribuciones.

}
