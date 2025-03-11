package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.server.roles.Permiso;

import java.util.List;
import java.util.Optional;

public interface IPermisoRepository {

    public List<Permiso> buscarTodos();
    public Optional<Permiso> buscarPorId(Long id);
    public void agregar(Permiso permiso);


}
