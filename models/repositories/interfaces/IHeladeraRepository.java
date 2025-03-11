package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.ModeloHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;

import java.util.List;
import java.util.Optional;

public interface IHeladeraRepository {
  Optional<Heladera> buscarPorDireccion(Direccion direccion);

  Optional<Heladera> buscarPorNombre(String nombre);

  List<Heladera> buscarTodas();

  void eliminarSolicitudesCaducadas();

  Optional<Heladera> buscarPorId(Long id);

  void eliminarHeladeraPorNombre(String nombre);

  List<PuntoHeladera> buscarPuntosHeladera();

  void guardarHeladera(Heladera heladera);

  void agregarModelo(ModeloHeladera modeloHeladera);

  List<ModeloHeladera> buscarModelos();

  Optional<ModeloHeladera> buscarModeloPorId(Long id);

}
