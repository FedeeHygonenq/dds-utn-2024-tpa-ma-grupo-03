package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.Contribucion;

import java.util.List;
import java.util.Optional;

public interface IContribucionRepository {

    List<Contribucion> obtenerContribuciones();

    void agreagarContribucion(Contribucion contribucion);

    Optional<Contribucion> obtenerContribucionPorId(Long id);

    void agregarCodigo(String codigo);

    void eliminarCodigo(String codigo);

    Boolean existeCodigo(String codigo);

}
