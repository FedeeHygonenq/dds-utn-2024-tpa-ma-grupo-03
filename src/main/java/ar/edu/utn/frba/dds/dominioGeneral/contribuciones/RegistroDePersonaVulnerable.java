package ar.edu.utn.frba.dds.dominioGeneral.contribuciones;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.PersonaEnSituacionVulnerable;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
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
@Table(name = "registro_persona_vulnerable")
public class RegistroDePersonaVulnerable extends Contribucion{


    @OneToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "id_tarjeta", referencedColumnName = "id", nullable = false)
    private Tarjeta tarjeta;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @OneToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "id_persona_en_situacion_vulnerable", referencedColumnName = "id")
    private PersonaEnSituacionVulnerable personaEnSituacionVulnerable;

    public RegistroDePersonaVulnerable(Tarjeta tarjeta, PersonaEnSituacionVulnerable personaEnSituacionVulnerable, Colaborador colaborador) {
        this.tarjeta = tarjeta;
        this.personaEnSituacionVulnerable = personaEnSituacionVulnerable;
        this.setColaborador(colaborador);
        this.fechaRegistro = LocalDate.now();
    }
    public Boolean contribuidorAceptado() {
        return this.getColaborador().getTipoColaborador() == HUMANO;
    }

    public Double multiplicador()
    {
        return LectorProperties.getDoublePropertie("entregaTarjeta");
    }

    @Override
    public Double puntos(Colaborador colaborador)
    {
        return this.multiplicador();
    }


}
