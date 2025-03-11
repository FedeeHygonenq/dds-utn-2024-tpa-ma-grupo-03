package ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoAccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.sql.ordering.antlr.ColumnReference;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "registro_uso_de_tarjeta")
public class RegistroUsoDeTarjeta {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "fecha", columnDefinition = "TIMESTAMP")
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_heladera", referencedColumnName = "id", nullable = false)
    private Heladera heladeraObjetivo;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Vianda vianda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAccion tipoUsoDeTarjeta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_apertura", nullable = false)
    private MotivoApertura tipoApertura;


    public RegistroUsoDeTarjeta(Heladera heladeraObjetivo, Vianda vianda,TipoAccion tipoUsoDeTarjeta, MotivoApertura motivoApertura) {
        this.heladeraObjetivo = heladeraObjetivo;
        this.fecha = LocalDateTime.now();
        this.vianda = vianda;
        this.tipoUsoDeTarjeta = tipoUsoDeTarjeta;
        this.tipoApertura = motivoApertura;
    }
}
