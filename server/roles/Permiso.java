package ar.edu.utn.frba.dds.server.roles;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permiso")

public class Permiso {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;


    public Boolean tieneMismoNombre(String nombre)
    {
        return this.nombre.equals(nombre);
    }

}
