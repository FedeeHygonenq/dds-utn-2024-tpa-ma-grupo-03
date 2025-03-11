package ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "canje")
public class Canje {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "fecha", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_oferta_canjeada", referencedColumnName = "id", nullable = false)
    private Oferta ofertaCanjeada;

    @ManyToOne
    @JoinColumn(name = "id_colaborador", referencedColumnName = "id", nullable = false)
    private Colaborador colaborador;


}
