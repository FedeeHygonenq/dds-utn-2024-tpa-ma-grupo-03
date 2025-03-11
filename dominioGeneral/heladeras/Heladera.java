package ar.edu.utn.frba.dds.dominioGeneral.heladeras;


import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoAccion;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.IntentoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.RegistroApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.SolicitudDeApertura;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.PermisoAperturaException;
import lombok.*;


import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "heladera")
public class Heladera {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @Column(name = "puntoEstrategico", nullable = false)
    private PuntoHeladera puntoEstrategico;

    @Column(name = "fechaFuncionamiento", columnDefinition = "DATE", nullable = false)
    private LocalDate fechaFuncionamiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_heladera", nullable = false)
    private EstadoHeladera estado;

    @OneToOne
    @JoinColumn(name = "modelo", referencedColumnName = "id", nullable = false)
    private ModeloHeladera modelo;

    @Column(name = "mesesActiva", columnDefinition = "INTEGER", nullable = false)
    private Integer mesesActiva;
    //Su función es acumular los meses asumiendo que puede haber un plazo de inactividad en el medio.

    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "id_heladera")
    @Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
    private List<Vianda> viandas;

    @OneToMany
    @Cascade(CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private List<RegistroApertura> aperturas;

    @OneToMany
    @Cascade(CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "id_heladera")
    private List<SolicitudDeApertura> solicitudesDeApertura;

    @OneToMany
    @Cascade(CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "id_heladera")
    private List<RegistroTecnico> reparacion;


    public Heladera(PuntoHeladera puntoHeladera, ModeloHeladera modeloHeladera) {
        this.puntoEstrategico = puntoHeladera;
        this.modelo = modeloHeladera;
        this.fechaFuncionamiento = LocalDate.now();
        this.estado = EstadoHeladera.ACTIVA;
        this.mesesActiva = 0;
        this.viandas = new ArrayList<>();
        this.aperturas = new ArrayList<>();
        this.solicitudesDeApertura = new ArrayList<>();
        this.reparacion = new ArrayList<>();
    }

    public Integer capacidadOcupada() {
        return this.viandas.size();
    }

    public Integer capacidadDisponible() {
        return (this.modelo.getCapacidadEnViandas() - this.capacidadOcupada());
    }

    public void ingresarVianda(Vianda vianda) {
        this.viandas.add(vianda);
    }

    public void retirarVianda(Vianda vianda) {
        this.viandas.remove(vianda);
    }

    public Integer calcularMesesActiva(LocalDate fechaCargo) throws IllegalArgumentException {
        LocalDate fechaHoy = LocalDate.now();
        if (fechaCargo.isAfter(fechaHoy)) {
            throw new IllegalArgumentException("La fecha de cargo no puede ser mayor que la fecha de hoy");
        }
        Period periodo = Period.between(fechaCargo, fechaHoy);
        this.mesesActiva = periodo.getMonths();
        return this.mesesActiva;
    }

    public void recibirIntentoApertura(IntentoApertura intentoApertura) throws PermisoAperturaException {

        SolicitudDeApertura solicitud = buscarSolicitudDeApertura(intentoApertura);
        if (solicitud == null) {
            throw new PermisoAperturaException("No tienes permiso para abrir la heladera");
        }
        if (caducoTiempoDeEjecucion(solicitud)) {
            throw new PermisoAperturaException("Caduco el tiempo para la apertura");
        }
        if (solicitud.getFueRealizada()) {
            throw new PermisoAperturaException("La apertura ya fue realizada anteriormente");
        }
        solicitud.setFueRealizada(Boolean.TRUE);
        aperturas.add(RegistroApertura.builder().
                fecha(LocalDateTime.now()).
                motivo(obtenerMotivoApertura(solicitud.getAccionARealizar())).build());

    }

    public void agregarSolicitudAperturaEnHeladera(SolicitudDeApertura solicitudDeApertura) {
        this.solicitudesDeApertura.add(solicitudDeApertura);
    }

    public void agregarReparacion(RegistroTecnico reparacion) {
        this.reparacion.add(reparacion);
    }

    public Tecnico tecnicoMasCercano(List<Tecnico> tecnicos) {
        List<Direccion> direccionesTecnicos = tecnicos.stream().map(tecnico -> tecnico.getAreaCobertura().getPuntoCentral()).toList();
        Direccion direcTecnicoMasCercano = ServiceLocator.calculadoraDistancia()
                .obtenerDireccionMasCercana(puntoEstrategico.getDireccion(), direccionesTecnicos);
        return buscarTecnicoEnDireccion(direcTecnicoMasCercano, tecnicos);

    }

    public Boolean caducoTiempoDeEjecucion(SolicitudDeApertura solicitudDeApertura) {

        Integer tiempoLimite = LectorProperties.getIntegerPropertie("tiempoParaEjecutar");

        Duration duracion = Duration.between(solicitudDeApertura.getHoraInicio(), LocalDateTime.now());
        long horasDiferencia = duracion.toHours();
        return horasDiferencia >= tiempoLimite;
    }

    private SolicitudDeApertura buscarSolicitudDeApertura(IntentoApertura intentoApertura) {
        for (SolicitudDeApertura solicitud : solicitudesDeApertura) {
            if (this.compararIntentoApertura(solicitud, intentoApertura)) {
                return solicitud;
            }
        }
        return null;
    }

    private Boolean compararIntentoApertura(SolicitudDeApertura solicitud, IntentoApertura intentoApertura) {
        return solicitud.getHeladera().equals(intentoApertura.getHeladera()) &&
                solicitud.getTarjeta().equals(intentoApertura.getTarjeta())
                && solicitud.getAccionARealizar() == intentoApertura.getTipoAccion()
                && solicitud.getTipoApertura() == intentoApertura.getMotivoApertura();
    }

    private Tecnico buscarTecnicoEnDireccion(Direccion direccion, List<Tecnico> tecnicos) {
        for (Tecnico tecnico : tecnicos) {
            if (tecnico.getAreaCobertura().getPuntoCentral().equals(direccion)) {
                return tecnico;
            }
        }
        return null;
    }

    private MotivoApertura obtenerMotivoApertura(TipoAccion accion) {
        if (accion == TipoAccion.DONAR_VIANDA) {
            return MotivoApertura.INGRESO_VIANDA;
        } else {
            return MotivoApertura.RETIRO_VIANDA;
        }
    }
}
