package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;

import java.util.List;
import java.util.Optional;

public interface IColaboradorRepository {

   List<Colaborador> buscarTodos();

   Optional<Colaborador> buscarPorId(Long id);

   Optional<Colaborador> buscarPorNumeroDeDocumento(String numeroDeDocumento);

   Optional<Colaborador> buscarPorTarjeta(String tarjeta);

   void guardarOActualizar(Colaborador colaborador);

   void eliminarTodos();

    Optional<Colaborador> buscarPorNombreDeUsuario(String nombreUsuario);

    Optional<List<Heladera>> buscarHeladerasPorColaborador(Long idColabortador);
}
