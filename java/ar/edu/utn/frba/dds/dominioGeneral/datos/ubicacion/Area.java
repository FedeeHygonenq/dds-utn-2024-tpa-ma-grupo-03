package ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Area {


    @Embedded
    @Column(name="puntoCentral",nullable = false)
    private Direccion puntoCentral;

    @Column(name = "kmALaRedondo",nullable = false)
    private Integer kmALaRedonda;


}
