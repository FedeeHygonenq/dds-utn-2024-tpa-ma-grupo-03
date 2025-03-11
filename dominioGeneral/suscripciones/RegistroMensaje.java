package ar.edu.utn.frba.dds.dominioGeneral.suscripciones;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="registro_mensaje")
public class RegistroMensaje {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "fecha", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "mensaje", columnDefinition = "VARCHAR(255)", nullable = false)
    private String mensaje;

    @ManyToMany
    @JoinTable(
        name = "registro_mensaje_x_colaborador",
        joinColumns = @JoinColumn(name = "id_registro_mensaje", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_colaborador")
    )
    private List<Colaborador> destinatarios;

    public RegistroMensaje(String msj, List<Colaborador> colaboradores){
        fecha = LocalDateTime.now();
        mensaje = msj;
        destinatarios = new ArrayList<>(colaboradores);
    }
}
