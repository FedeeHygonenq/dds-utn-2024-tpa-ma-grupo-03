package ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoDocumento;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Contacto;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import lombok.*;

import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "tecnicos")
public class Tecnico{
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(255)", nullable = false)
    private String cuil;

    @OneToOne
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "usuario")
    private Usuario usuario;

    @Column(name = "nombre", columnDefinition = "VARCHAR(255)", nullable = false)
    private String nombre;
    @Column(name = "apellido", columnDefinition = "VARCHAR(255)", nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "documento", columnDefinition = "VARCHAR(255)", nullable = false)
    private String nroDocumento;


    @OneToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name ="id_contacto", referencedColumnName = "id")
    private Contacto contacto;

    @Embedded
    private Area areaCobertura;

    public Tecnico(String cuil, Usuario usuario, String nombre, String apellido, TipoDocumento tipoDocumento, String nroDocumento, Contacto contacto, Area areaCobertura) {
        this.cuil = cuil;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.contacto = contacto;
        this.areaCobertura = areaCobertura;
    }

    public Boolean cubreUbicacion(Direccion direccion){
        Double distancia = ServiceLocator.calculadoraDistancia().distanciaEntrePuntos(areaCobertura.getPuntoCentral(),direccion);
        return distancia <= areaCobertura.getKmALaRedonda();
    }

    public void serNotificadoPor(Notificacion notificacion){
        contacto.getMedioDeComunicacion().enviar(notificacion,contacto.getValor());
    }

}
