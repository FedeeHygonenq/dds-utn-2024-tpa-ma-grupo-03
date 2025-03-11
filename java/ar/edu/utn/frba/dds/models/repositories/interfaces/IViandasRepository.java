package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import java.util.List;
import java.util.Optional;

public interface IViandasRepository {
    void eliminarTodos();

    void guardarVianda(Vianda vianda);

    void eliminarVianda(Vianda vianda);

    void guardarViandas(List<Vianda> viandas);

    List<Vianda> buscarTodas();

    Optional<Vianda> buscarPorId(Long id);
}
