package ar.edu.utn.frba.dds.dominioGeneral.usuarios;

import ar.edu.utn.frba.dds.server.roles.TipoRol;
import javax.persistence.GeneratedValue;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {
    @Id
    private String usuario;

    @Column(name = "contrasenia")
    private String contrasenia;

    @Column(name = "estaActivo")
    private Boolean estaActivo;

    @Column(name = "esAdmin")
    private Boolean esAdmin;

    @Column(name = "tipoRol")
    private TipoRol tipoRol;

    public Usuario(String usuario, String contrasenia, TipoRol tipoRol) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.estaActivo = true;
        this.esAdmin = tipoRol.equals(TipoRol.ADMINISTRADOR);
        this.tipoRol = tipoRol;
    }

    public Usuario(String usuario, String contrasenia) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.estaActivo = true;
        this.esAdmin = false;
        this.tipoRol = null;
    }
    //como hacemos con tema admin

    public void cambiarEstadoActivo(){
        this.estaActivo = !(this.estaActivo);
    }

}
