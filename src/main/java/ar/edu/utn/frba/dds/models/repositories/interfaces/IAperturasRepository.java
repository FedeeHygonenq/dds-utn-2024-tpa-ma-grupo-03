package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.RegistroApertura;

import java.time.LocalDateTime;
import java.util.List;

public interface IAperturasRepository {

  public List<RegistroApertura> buscarTodas();

  public void agregar(RegistroApertura registro);

  public List<RegistroApertura> buscarPorFecha(LocalDateTime fecha);

  public List<RegistroApertura> buscarPorMotivoApertura(MotivoApertura motivo);

}
