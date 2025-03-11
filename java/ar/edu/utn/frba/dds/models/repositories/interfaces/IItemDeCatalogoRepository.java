package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.productosYServicios.ItemDeCatalogo;
import java.util.List;
import java.util.Optional;

public interface IItemDeCatalogoRepository {

  public List<ItemDeCatalogo> buscarTodos();

  public Optional<ItemDeCatalogo> buscarPorId(Long id);

}
