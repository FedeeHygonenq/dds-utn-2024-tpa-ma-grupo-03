package ar.edu.utn.frba.dds.dominioGeneral.usuarios;

import ar.edu.utn.frba.dds.models.converters.MedioDeComunicacionAttributeConverter;
import ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion.MedioDeComunicacion;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="contacto")
public class Contacto{
    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = MedioDeComunicacionAttributeConverter.class)
    @Column(name= "medioDeComunicacion")
    public MedioDeComunicacion medioDeComunicacion;

    @Column(name="valor_particular", columnDefinition = "VARCHAR(255)")
    public String valor;

    public Contacto(MedioDeComunicacion medioDeComunicacion, String valor) {
        this.medioDeComunicacion = medioDeComunicacion;
        this.valor = valor;
    }
}
