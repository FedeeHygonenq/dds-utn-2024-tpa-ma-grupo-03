package ar.edu.utn.frba.dds.dominioGeneral.usuarios;

import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.FormularioRespondido;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoDocumento;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="persona_en_situacion_vulnerable")
public class PersonaEnSituacionVulnerable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @Cascade(CascadeType.ALL)
    @JoinColumn(name ="id_formulario_respondido" ,referencedColumnName = "id")
    private FormularioRespondido datosAdicionales;

    @Column(name ="nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name="fechaDeRegistro", columnDefinition = "DATE",nullable = false)
    private LocalDate fechaDeRegistro;

    @Column(name="fechaDeNacimiento", columnDefinition = "DATE", nullable = false)
    private LocalDate fechaDeNacimiento;

    @Column(name ="fechaDesvinculoDeAdulto", columnDefinition = "DATE")
    private LocalDate fechaDesvinculacionDeAdulto;

    @Embedded
    private Direccion domicilio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_colaborador", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "numeroDeDocumento",nullable = false)
    private String numeroDeDocumento;

    @OneToOne
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "tarjeta", referencedColumnName = "id")
    private Tarjeta tarjeta;

    @ManyToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    private List<PersonaEnSituacionVulnerable> menoresACargo;

    public PersonaEnSituacionVulnerable(FormularioRespondido formularioRespondido, String nombre, String apellido, LocalDate fechaDeNacimiento, Direccion domicilio, TipoDocumento tipoDocumento, String numeroDeDocumento, Tarjeta tarjeta, List<PersonaEnSituacionVulnerable> menoresACargo) {
        this.datosAdicionales = formularioRespondido;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeRegistro = LocalDate.now();
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.domicilio = domicilio;
        this.tipoDocumento = tipoDocumento;
        this.numeroDeDocumento = numeroDeDocumento;
        this.tarjeta = tarjeta;
        this.menoresACargo = menoresACargo;
    }

    //Constructor para los menores
    public PersonaEnSituacionVulnerable(String nombre, String apellido, LocalDate fechaDeNacimiento, Direccion domicilio, TipoDocumento tipoDocumento, String numeroDeDocumento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeRegistro = LocalDate.now();
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.domicilio = domicilio;
        this.tipoDocumento = tipoDocumento;
        this.numeroDeDocumento = numeroDeDocumento;;
        this.menoresACargo = new ArrayList<>();
    }

    public Integer edad(){
        return Period.between(this.fechaDeNacimiento, LocalDate.now()).getYears();
    }

    public Boolean tieneMenoresACargo(){
        return this.menoresACargo.size() > 0;
    }

    public Integer cantidadMenoresACargo(){
        return this.menoresACargo.size();
    }

    public Boolean esMenor(){
        return this.edad() < 18;
    }

    public void quitarMayoresDeLaLista(){
            this.menoresACargo = this.menoresACargo.stream()
                    .filter(persona -> persona.esMenor())
                    .collect(Collectors.toList());
        }
}

