package ar.edu.utn.frba.dds.dominioGeneral.datos.formulario;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "campo_formulario")
public class CampoFormulario {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "esObligatorio")
    private Boolean esObligatorio;

    @Column(name = "nombreDelCampo", columnDefinition = "VARCHAR(255)")
    private String nombreDelCampo;

    @Column(name = "descripcionDelCampo", columnDefinition = "VARCHAR(255)")
    private String descripcionDelCampo;

    public CampoFormulario(Boolean esObligatorio, String nombreDelCampo, String descripcionDelCampo){
        this.esObligatorio = esObligatorio;
        this.nombreDelCampo = nombreDelCampo;
        this.descripcionDelCampo = descripcionDelCampo;
    }

}
