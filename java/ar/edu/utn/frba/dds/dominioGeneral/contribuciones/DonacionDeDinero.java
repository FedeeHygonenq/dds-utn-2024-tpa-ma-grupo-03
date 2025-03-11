package ar.edu.utn.frba.dds.dominioGeneral.contribuciones;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import java.time.LocalDate;

import static ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador.HUMANO;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donacion_de_dinero")
public class DonacionDeDinero extends Contribucion{

    @Column(name = "fechaDeDonacion", nullable = false)
    private LocalDate fechaDeDonacion;

    @Column(name = "monto")
    private Double monto;

    @Column(name = "frecuencia")
    private Integer frecuencia;

    public DonacionDeDinero(Double monto, Integer frecuencia, Colaborador colaborador) {
        this.monto = monto;
        this.frecuencia = frecuencia;
        this.fechaDeDonacion = LocalDate.now();
        this.setColaborador(colaborador);

    }

    public Boolean contribuidorAceptado() {
        return this.getColaborador().getTipoColaborador() == TipoColaborador.JURIDICO || this.getColaborador().getTipoColaborador() == HUMANO;
    }

    public Double multiplicador() {

        return LectorProperties.getDoublePropertie("dineroDonado");
    }

    @Override
    public Double puntos(Colaborador colaborador) {
        return this.monto * this.multiplicador();
    }
}
