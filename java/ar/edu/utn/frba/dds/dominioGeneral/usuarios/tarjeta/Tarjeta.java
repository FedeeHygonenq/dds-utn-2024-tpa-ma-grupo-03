package ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoPersona;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.PersonaEnSituacionVulnerable;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.LimiteDeUsosAlcanzadoException;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tarjeta")
public class Tarjeta {

  @Id
  private String id;

  @OneToMany
  @JoinColumn(referencedColumnName = "id")
  private List<RegistroUsoDeTarjeta> historialDeUso;

  @Enumerated(EnumType.STRING)
  @Column(name = "titular")
  private TipoPersona titular;

  @Column(name = "usosActuales")
  private Integer usosActuales;

  @Column(name = "fechaDeAlta", columnDefinition = "DATE")
  private LocalDate fechaDeAlta;

  @Column(name = "fechaDeBaja", columnDefinition = "DATE")
  private LocalDate fechaDeBaja;


  public Tarjeta(String id, TipoPersona titular) {
    this.id = id;
    this.historialDeUso = new ArrayList<>();
    this.titular = titular;
    this.usosActuales = 0;
    this.fechaDeAlta = LocalDate.now();
  }

  public Integer usosPermitidosPorDia(PersonaEnSituacionVulnerable persona) {

    Integer base = LectorProperties.getIntegerPropertie("base");
    Integer porHijo = LectorProperties.getIntegerPropertie("porHijo");
    return (base + (porHijo * persona.cantidadMenoresACargo()));
  }


  public Integer usosRestantes(PersonaEnSituacionVulnerable persona) {
    return this.usosPermitidosPorDia(persona) - this.usosActuales;
  }

  public void reiniciarUsos() {
    this.usosActuales = 0;
  }

  public void registrarUso(PersonaEnSituacionVulnerable persona) {
    if (!this.permiteUso(persona)) {
      throw new LimiteDeUsosAlcanzadoException("Los usos permitidos ya fueron superados");
    }
    this.usosActuales += 1;
  }

  public Boolean permiteUso(PersonaEnSituacionVulnerable persona) {
    return this.usosActuales < this.usosPermitidosPorDia(persona);
  }

  public Boolean estaActiva() {
    return fechaDeBaja == null;
  }

  public void agregarUso(RegistroUsoDeTarjeta uso, PersonaEnSituacionVulnerable persona) {
    this.historialDeUso.add(uso);
    registrarUso(persona);
  }

  public void agregarUso(RegistroUsoDeTarjeta uso) {
    this.historialDeUso.add(uso);
  }
}
