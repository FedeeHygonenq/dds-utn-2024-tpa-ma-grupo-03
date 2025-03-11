package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.PersonaEnSituacionVulnerable;
import java.util.List;
import java.util.Optional;

public interface IPersonaVulnerableRepository {

  List<PersonaEnSituacionVulnerable> buscarTodos();

  void guardarPersonaVulnerable(PersonaEnSituacionVulnerable personaEnSituacionVulnerable);

  Optional<PersonaEnSituacionVulnerable> buscarPorId(Integer id);

  Optional<PersonaEnSituacionVulnerable> buscarPorDocumento(String documento);

  Optional<PersonaEnSituacionVulnerable> buscarPorTarjetaId(String tarjeta);
}
