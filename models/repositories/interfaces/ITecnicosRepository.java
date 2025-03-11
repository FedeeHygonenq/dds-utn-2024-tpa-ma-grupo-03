package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.RegistroTecnico;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import java.util.List;
import java.util.Optional;

public interface ITecnicosRepository {

   List<Tecnico> buscarTodos();

   Optional<Tecnico> buscarPorCuil(String cuil);

   Optional<Tecnico> buscarPorId(Long id);

   Optional<Tecnico> traerPorUsuario(String usuario);

   void agregar(Tecnico tecnico);

   void agrearRegistro(RegistroTecnico registroTecnico);

   List<RegistroTecnico> buscarRegistros();

   List<RegistroTecnico> buscarRegistrosPorTecnico(Tecnico tecnico);

   List<RegistroTecnico> buscarRegistrosPorHeladera(Long heladeraId);

}
