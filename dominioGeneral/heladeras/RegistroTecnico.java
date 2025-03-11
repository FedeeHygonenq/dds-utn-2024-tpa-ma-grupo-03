package ar.edu.utn.frba.dds.dominioGeneral.heladeras;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import java.time.LocalDate;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "registro_tecnico")
public class RegistroTecnico {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDate fechaVisita;

    @Column( columnDefinition = "VARCHAR(255)",nullable = false)
    private String descripcionTrabajado;

    @Column(nullable = false)
    private Boolean trabajoTerminado;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Tecnico tecnico;

    @Column(name = "pathFoto", columnDefinition = "VARCHAR(255)")
    private String pathFoto;


    public RegistroTecnico(String descripcion, Tecnico tecnico, String pathFoto, Boolean trabajoTerminado, LocalDate fechaVisita) {
        this.descripcionTrabajado = descripcion;
        this.tecnico = tecnico;
        this.pathFoto = pathFoto;
        this.trabajoTerminado = trabajoTerminado;
        this.fechaVisita = fechaVisita;
    }

}


