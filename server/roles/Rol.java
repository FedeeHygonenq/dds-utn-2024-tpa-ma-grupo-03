package ar.edu.utn.frba.dds.server.roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoRol tipo;


    @ManyToMany
    @JoinTable(
            name = "rol_x_permiso",
            joinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_permiso", referencedColumnName = "id")
    )
    private List<Permiso> permisos;

    public void agregarPermisos(Permiso ... permisos) {
        Collections.addAll(this.permisos, permisos);
    }

    public boolean tenesPermiso(String permisoSolicitado) {
        return this.permisos.stream().anyMatch(permiso -> permiso.tieneMismoNombre(permisoSolicitado));
    }


}
