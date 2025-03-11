package ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.PuntosNoSuficientesException;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "billetera_de_puntos")
@Builder
@AllArgsConstructor

public class BilleteraDePuntos {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "puntosActuales")
  private Double puntosActuales;

  @Column(name = "puntosGastados")
  private Double puntosGastados;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(referencedColumnName = "id")
  private List<Canje> historialDePuntosGastados;


  public BilleteraDePuntos() {
    this.puntosActuales = 0.0;
    this.puntosGastados = 0.0;
    this.historialDePuntosGastados = new ArrayList<>();
  }
  public Double calcularPuntos(Colaborador colaborador) {
    puntosActuales = colaborador.getContribuciones().stream()
        .mapToDouble(contribucion -> contribucion.puntos(colaborador))
        .sum() - this.puntosGastados;
    return puntosActuales;
  }

  public Boolean transaccionPermitida(Oferta oferta) {
      return this.puntosActuales - oferta.puntosNecesarios() >= 0;
  }

  public void realizarTransaccion(Oferta oferta, Colaborador colaborador) {

    if(!transaccionPermitida(oferta)) {
      throw new PuntosNoSuficientesException("¡No tenés puntos suficientes para realizar esta transacción!");
    }

    LocalDateTime fechaCanje = LocalDateTime.now();

    Canje canje = Canje.builder()
        .fecha(fechaCanje).ofertaCanjeada(oferta).colaborador(colaborador).build();

    this.historialDePuntosGastados.add(canje);
    this.puntosGastados += oferta.puntosNecesarios();
    calcularPuntos(colaborador);
  }
}