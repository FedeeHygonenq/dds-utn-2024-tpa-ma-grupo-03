package ar.edu.utn.frba.dds.dominioGeneral.contribuciones;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


@EqualsAndHashCode
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "contribucion")
@Getter
@Setter
public abstract class Contribucion{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn ( name = "id_colaborador", nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Colaborador colaborador;

    public abstract Boolean contribuidorAceptado();
    public abstract Double multiplicador();
    public abstract Double puntos(Colaborador colaborador);
}
