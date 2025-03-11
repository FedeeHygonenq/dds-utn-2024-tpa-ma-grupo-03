package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registro_temperatura")
public class RegistroTemperatura {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "fecha", columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime fecha;

  @Column (name = "temperatura", nullable = false)
  private Double temperatura;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "heladera_id", referencedColumnName = "id", nullable = false)
  private Heladera heladera;
}
