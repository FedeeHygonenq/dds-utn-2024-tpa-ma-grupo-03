package ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoAccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "intento_apertura")
public class IntentoApertura {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "fecha", columnDefinition = "TIMESTAMP")
  private LocalDateTime fecha;

  @ManyToOne
  @JoinColumn(name = "id_tarjeta", referencedColumnName = "id", nullable = false)
  private Tarjeta tarjeta;

  @ManyToOne
  @JoinColumn(name = "id_heladera", referencedColumnName = "id", nullable = false)
  private Heladera heladera;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_accion")
  private TipoAccion tipoAccion;

  @Enumerated(EnumType.STRING)
  @Column(name = "motivo_apertura")
  private MotivoApertura motivoApertura;

  public IntentoApertura(Tarjeta tarjeta, Heladera heladera, TipoAccion tipoAccion, MotivoApertura motivoApertura) {
    this.tarjeta = tarjeta;
    this.heladera = heladera;
    this.fecha = LocalDateTime.now();
    this.tipoAccion = tipoAccion;
    this.motivoApertura = motivoApertura;
  }
}
