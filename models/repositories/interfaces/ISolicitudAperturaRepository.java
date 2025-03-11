package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.SolicitudDeApertura;
import java.util.List;

public interface ISolicitudAperturaRepository {

  public void agregar(SolicitudDeApertura solicitudDeApertura);
  public List<SolicitudDeApertura> buscarTodos();
}
