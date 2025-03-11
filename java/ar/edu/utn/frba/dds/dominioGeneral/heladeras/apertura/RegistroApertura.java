package ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoAccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import java.time.LocalDateTime;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Getter
@Setter
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registro_apertura")
public class RegistroApertura {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "fecha", columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime fecha;

  @Enumerated(EnumType.STRING)
  @Column(name="motivoApertura",nullable = false)
  private MotivoApertura motivo;


}
