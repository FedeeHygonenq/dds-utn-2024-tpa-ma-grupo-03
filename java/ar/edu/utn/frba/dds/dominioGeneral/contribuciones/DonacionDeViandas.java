package ar.edu.utn.frba.dds.dominioGeneral.contribuciones;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import static ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador.HUMANO;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donacion_de_vianda")
public class DonacionDeViandas extends Contribucion {


    @OneToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "id_vianda", referencedColumnName = "id")
    private Vianda vianda;

    @Column(name = "fechaDonacion", columnDefinition = "DATE")
    private LocalDate fechaDeDonacion;

    public DonacionDeViandas(Vianda vianda, Colaborador colaborador) {
        this.vianda = vianda;
        this.fechaDeDonacion = LocalDate.now();
        this.setColaborador(colaborador);
    }

    public Boolean contribuidorAceptado() {
        return this.getColaborador().getTipoColaborador() == HUMANO;
    }

    public Double multiplicador() {

        return LectorProperties.getDoublePropertie("viandasDonadas");
    }

    @Override
    public Double puntos(Colaborador colaborador) {
        return this.multiplicador();
    }

}
