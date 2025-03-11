package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.SubDesperfectoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.SubFaltanNViandas;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.SubQuedanNViandas;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.Suscripcion;
import java.util.List;
import java.util.Optional;

public interface ISuscripcionRepository {
   List<Suscripcion> buscarTodas();
   void agregar(Suscripcion suscripcion);
   List<SubDesperfectoHeladera> buscarTodasSubDesperfectoHeladera();
   List<SubFaltanNViandas> buscarTodasSubFaltanNViandas();
   List<SubQuedanNViandas> buscarTodasSubQuedanNViandas();

   Optional<SubFaltanNViandas> buscarSubFaltanNViandasPorAreaYViandas(Area area, Integer cantViandas);
   Optional<SubQuedanNViandas> buscarSubQuedanNViandasPorAreaYViandas(Area area, Integer cantViandas);
   Optional<SubDesperfectoHeladera> buscarSubDesperfectoHeladeraPorArea(Area area);
}
