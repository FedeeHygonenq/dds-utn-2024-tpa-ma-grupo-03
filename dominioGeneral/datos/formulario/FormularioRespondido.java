package ar.edu.utn.frba.dds.dominioGeneral.datos.formulario;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="formulario_respondido")
public class FormularioRespondido {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "formularioRespondido_por_campoRespondido",
        joinColumns = @JoinColumn(name = "id_formulario_respondido", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_campo_respondido", referencedColumnName = "id")
    )
    private List<CampoRespondido> camposRespondidos;

    @Column(name = "fechaRegistro", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaDeRegistro;

    public void agregarCampo(CampoRespondido nuevoCampo){
        this.camposRespondidos.add(nuevoCampo);
    }

    public FormularioRespondido(List<CampoRespondido> camposRespondidos){
        this.camposRespondidos = camposRespondidos;
        this.fechaDeRegistro = LocalDateTime.now();
    }

}
