package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.server.roles.Rol;

import java.util.List;
import java.util.Optional;

public interface IOfertaRepository {
    public List<Oferta> buscarTodos();
    public Optional<Oferta> buscarPorId(Long id);
    public void agregar(Oferta oferta);
}
