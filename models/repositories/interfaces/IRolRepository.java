package ar.edu.utn.frba.dds.models.repositories.interfaces;


import ar.edu.utn.frba.dds.server.roles.Rol;

import java.util.List;
import java.util.Optional;

public interface IRolRepository {

    public List<Rol> buscarTodos();
    public Optional<Rol> buscarPorId(Long id);
    public void agregar(Rol rol);
}
