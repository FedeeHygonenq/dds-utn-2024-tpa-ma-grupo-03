package ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoIncidente;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registros_incidentes")
public class RegistroIncidente {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "fecha", columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime fecha;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipoIncidente", nullable = false)
  private TipoIncidente incidente;

  @OneToOne
  @JoinColumn(name = "id_heladera", referencedColumnName = "id", nullable = false)
  private Heladera heladera;

  @Column(name = "descripcion", columnDefinition = "VARCHAR(255)")
  private String descripcion;

  @Column(name = "pathFoto", columnDefinition = "VARCHAR(255)")
  private String pathFoto;

  @OneToOne
  @JoinColumn(name = "id_colaborador", referencedColumnName = "id")
  private Colaborador colaboradorQueReporto;

  public RegistroIncidente(TipoIncidente incidente, Heladera heladera, String descripcion, String pathFoto, Colaborador colaborador) {
    this.incidente = incidente;
    this.heladera = heladera;
    this.fecha = LocalDateTime.now();
    this.descripcion = descripcion;
    this.pathFoto = pathFoto;
    this.colaboradorQueReporto = colaborador;
  }

  public RegistroIncidente(TipoIncidente incidente, Heladera heladera, String descripcion) {
    this.incidente = incidente;
    this.heladera = heladera;
    this.fecha = LocalDateTime.now();
    this.descripcion = descripcion;
  }
}
