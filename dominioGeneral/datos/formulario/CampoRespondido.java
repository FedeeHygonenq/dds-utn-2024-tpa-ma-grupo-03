package ar.edu.utn.frba.dds.dominioGeneral.datos.formulario;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "campo_respondido")
public class CampoRespondido {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "id_campo_formulario", referencedColumnName = "id")
    private CampoFormulario campoFormulario;

    @Column(name = "valorRespondido", columnDefinition = "VARCHAR(255)")
    private String valorRespondido;

    public CampoRespondido(CampoFormulario campoFormulario, String respuesta){
        this.campoFormulario = campoFormulario;
        this.valorRespondido = respuesta;
    }

}
