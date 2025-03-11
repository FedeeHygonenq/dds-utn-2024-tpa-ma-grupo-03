package ar.edu.utn.frba.dds.dominioGeneral.heladeras;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PuntoHeladera {

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Embedded
    private Direccion direccion;

}
