package ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

@Embeddable
public class Direccion{


    @Column(name = "longitud")
    private Double longitud;
    @Column(name="latitud")
    private Double latitud;

}
