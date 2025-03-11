package ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.*;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.CampoRespondido;
import ar.edu.utn.frba.dds.dominioGeneral.datos.formulario.FormularioRespondido;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoDocumento;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoOrganizacion;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoPersona;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoRubro;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Contacto;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos.BilleteraDePuntos;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import java.util.ArrayList;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "colaborador")
public class Colaborador {
  @Id
  @GeneratedValue
  private Long id;

  @OneToOne
  @Cascade(CascadeType.ALL)
  @JoinColumn(name = "id_usuario", referencedColumnName = "usuario")
  private Usuario usuario;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_colaborador")
  private TipoColaborador tipoColaborador;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_organizacion")
  private TipoOrganizacion tipoOrganizacion;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_documento")
  private TipoDocumento tipoDocumento;

  @Column(name = "numero_de_documento", columnDefinition = "varchar(20)")
  private String numeroDeDocumento;

  @OneToMany
  @Cascade(CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id", name="colaborador_id")
  private List<Contacto> mediosDeComunicacion;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Embedded
  private Direccion direccion;

  @Enumerated(EnumType.STRING)
  @Column(name = "rubro")
  private TipoRubro rubro;

  @Column(name = "razonSocial", columnDefinition = "VARCHAR(255)")
  private String razonSocial;

  @OneToOne
  @JoinColumn(name = "id_formulario_respondido", referencedColumnName = "id")
  @Cascade(CascadeType.ALL)
  private FormularioRespondido datos;

  @OneToMany(mappedBy = "colaborador", orphanRemoval = true)
  @Cascade(CascadeType.ALL)
  private List<Contribucion> contribuciones;

  @OneToOne
  @Cascade(CascadeType.ALL)
  @JoinColumn(name = "billeteraDePuntos", referencedColumnName = "id")
  private BilleteraDePuntos billeteraDePuntos;

  @OneToMany
  @Cascade(CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id", name = "colaborador_id")
  private List<Tarjeta> tarjetas;

  @Column(name = "fechaDeRegistro", columnDefinition = "DATE")
  private LocalDate fechaDeRegistro;

  public Colaborador(Usuario usuario, TipoColaborador tipoColaborador, TipoDocumento tipoDocumento, String nroDocumento,
                     List<Contacto> contactos, String nombre, String apellido, Direccion direccion, FormularioRespondido formularioRespondido, Tarjeta tarjeta) {
    this.usuario = usuario;
    this.tipoColaborador = tipoColaborador;
    this.tipoDocumento = tipoDocumento;
    this.numeroDeDocumento = nroDocumento;
    this.mediosDeComunicacion = contactos;
    this.nombre = nombre;
    this.apellido = apellido;
    this.direccion = direccion;
    this.datos = formularioRespondido;
    this.billeteraDePuntos = new BilleteraDePuntos();
    this.tarjetas = new ArrayList<>();
    if(tarjeta != null) {
      tarjetas.add(tarjeta);
    }
    this.contribuciones = new ArrayList<>();
    this.fechaDeRegistro = LocalDate.now();
  }

  public Colaborador(Usuario usuario, TipoColaborador tipoColaborador, TipoOrganizacion tipoOrganizacion, List<Contacto> contactos,
                     Direccion direccion, TipoRubro tipoRubro, String razonSocial, FormularioRespondido formularioRespondido) {
    this.usuario = usuario;
    this.tipoColaborador = tipoColaborador;
    this.tipoOrganizacion = tipoOrganizacion;
    this.mediosDeComunicacion = contactos;
    this.direccion = direccion;
    this.rubro = tipoRubro;
    this.razonSocial = razonSocial;
    this.datos = formularioRespondido;
    this.billeteraDePuntos = new BilleteraDePuntos();
    this.tarjetas = new ArrayList<>();
    this.contribuciones = new ArrayList<>();
    this.fechaDeRegistro = LocalDate.now();
  }

  public void agregarDatos(CampoRespondido campoRespondido) {
    this.datos.agregarCampo(campoRespondido);
  }

  public void cambiarPuntosPorOferta(Oferta oferta) {
    this.billeteraDePuntos.realizarTransaccion(oferta, this);
  }

  public void actualizarPuntosEnBilletera() {
    this.billeteraDePuntos.calcularPuntos(this);
  }

  public Double puntosDisponiblesEnBilletera() {
    actualizarPuntosEnBilletera();
    return this.billeteraDePuntos.getPuntosActuales();
  }

  public void agregarContribucion(Contribucion contribucion) {
    this.contribuciones.add(contribucion);
    this.actualizarPuntosEnBilletera();
  }

  public Integer viandasDonadas() {
    Integer viandasDonadas = 0;

    for (Contribucion contribucion : contribuciones) {
      if (contribucion instanceof DonacionDeViandas) {
        viandasDonadas++;
      }
    }

    return viandasDonadas;
  }


  public void serNotificadoPor(Notificacion notificacion) {
    for (Contacto contacto : mediosDeComunicacion) {
      contacto.getMedioDeComunicacion().enviar(notificacion, contacto.getValor());
    }
  }

  public void darDeBajaTarjeta() {
    Tarjeta tarjeta = tarjetas.get(tarjetas.size() - 1);
    tarjeta.setFechaDeBaja(LocalDate.now());
  }

  public void darDeAltaTarjeta(Tarjeta tarjeta) {
    tarjetas.add(tarjeta);
  }

  public Boolean esHumano() {
    return this.tipoColaborador == TipoColaborador.HUMANO;
  }

  public Tarjeta tarjetaDeColaborador() {
    return tarjetas.stream().filter(tarjeta -> tarjeta.getTitular().equals(TipoPersona.Colaborador)).findFirst().orElse(null);
  }
}
