package ar.edu.utn.frba.dds.dominioGeneral.DTOs;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.ModeloHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.RegistroApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.RegistroTecnico;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.SolicitudDeApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeladeraDTOOut {

  private PuntoHeladera puntoEstrategico;
  private LocalDate fechaFuncionamiento;
  private EstadoHeladera estado;
  private ModeloHeladera modelo;
  private Integer mesesActiva;
  private List<Vianda> viandas;
  private List<RegistroApertura> aperturas;
  private List<SolicitudDeApertura> solicitudesDeApertura;
  private List<RegistroTecnico> reparacion;
}
