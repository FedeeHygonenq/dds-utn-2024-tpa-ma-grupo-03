package ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoAccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="solicitud_apertura")
public class SolicitudDeApertura {
    @Id
    @GeneratedValue
    private  Long id;

    @ManyToOne
    @JoinColumn(name = "id_tarjeta", referencedColumnName = "id", nullable = false)
    private Tarjeta tarjeta;

    @Column(name = "horaInicio", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime horaInicio;

    @ManyToOne
    @JoinColumn(name = "id_heladera", referencedColumnName = "id", nullable = false)
    private Heladera heladera;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_apertura", nullable = false)
    private MotivoApertura tipoApertura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_accion", nullable = false)
    private TipoAccion accionARealizar;

    @Column(name = "fueRealizada")
    private Boolean fueRealizada;

    public SolicitudDeApertura(Tarjeta tarjeta, Heladera heladera, MotivoApertura tipoApertura, TipoAccion accionARealizar) {
        this.tarjeta = tarjeta;
        this.horaInicio = LocalDateTime.now();
        this.heladera = heladera;
        this.tipoApertura = tipoApertura;
        this.accionARealizar = accionARealizar;
        this.fueRealizada = false;
    }
}
