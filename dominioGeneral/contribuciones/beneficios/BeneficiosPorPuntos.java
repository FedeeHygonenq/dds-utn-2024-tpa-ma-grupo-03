package ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.Contribucion;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import lombok.*;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "beneficios_por_puntos") // de las empresas dan ofertas por puntos
public class BeneficiosPorPuntos extends Contribucion {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "id_beneficio")
    @Cascade(CascadeType.ALL)
    private List<Oferta> ofertas;

    public BeneficiosPorPuntos(Colaborador colaborador, List<Oferta> ofertas) {
        this.ofertas = ofertas;
        this.setColaborador(colaborador);
    }

    public BeneficiosPorPuntos(Colaborador colaborador) {
        this.setColaborador(colaborador);
        this.ofertas = new ArrayList<>();
    }

    public Boolean contribuidorAceptado() {
        return this.getColaborador().getTipoColaborador() == TipoColaborador.JURIDICO;
    }

    public Double multiplicador() {
        return LectorProperties.getDoublePropertie("beneficiosPorPuntos");
    }

    @Override
    public Double puntos(Colaborador colaborador) {
        return this.multiplicador();
    }
}
