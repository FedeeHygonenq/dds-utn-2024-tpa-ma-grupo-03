package ar.edu.utn.frba.dds.dominioGeneral.contribuciones;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.MotivoDistribucion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.time.LocalDate;

import static ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador.HUMANO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="distribucion_de_viandas")
public class DistribucionDeViandas extends Contribucion {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_heladera_origen", referencedColumnName = "id", nullable = false)
    private Heladera heladeraOrigen;

    @ManyToOne
    @JoinColumn(name="id_heladera_destino", referencedColumnName = "id", nullable = false)
    private Heladera heladeraDestino;


    @Column(name="cantidad_viandas")
    private Integer cantViandas;

    @Enumerated(EnumType.STRING)
    @Column(name="motivoDistribucion",nullable = false)
    private MotivoDistribucion motivoDistribucion;

    @Column(name="fechaDistribucion", columnDefinition = "DATE", nullable = false)
    private LocalDate fechaDistribucion;

    public DistribucionDeViandas(Heladera heladeraOrigen, Heladera heladeraDestino, Integer cantViandas, MotivoDistribucion motivoDistribucion, Colaborador colaborador) {
        this.heladeraOrigen = heladeraOrigen;
        this.heladeraDestino = heladeraDestino;
        this.cantViandas = cantViandas;
        this.motivoDistribucion = motivoDistribucion;
        this.fechaDistribucion = LocalDate.now();
        this.setColaborador(colaborador);
    }

    public Boolean contribuidorAceptado() {
        return this.getColaborador().getTipoColaborador() == HUMANO;
    }

    public Double multiplicador() {

        return LectorProperties.getDoublePropertie("viandasDistribuidas");
    }

    @Override
    public Double puntos(Colaborador colaborador)
    {
        return this.cantViandas * this.multiplicador();
    }
}




