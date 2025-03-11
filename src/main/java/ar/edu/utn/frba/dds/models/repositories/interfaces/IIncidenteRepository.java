package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;
import java.util.List;

public interface IIncidenteRepository {

   List<RegistroIncidente> buscarTodos();
   void agregarIncidente (RegistroIncidente registroIncidente);

  List<RegistroIncidente> buscarPorHeladera(Heladera heladera);
}
