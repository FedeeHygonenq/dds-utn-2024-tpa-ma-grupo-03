package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.ModeloHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IHeladeraRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HeladeraRepository implements IHeladeraRepository {

  Session session = HibernateUtil.getSessionFactory().openSession();
  Transaction transaction = null;

  @Override
  public Optional<Heladera> buscarPorDireccion(Direccion direccion) {


    Query<Heladera> query = session.createQuery("from Heladera where puntoEstrategico.direccion = :direccion", Heladera.class);
    query.setParameter("direccion", direccion);
    return query.getResultStream().findFirst();

  }

  @Override
  public void guardarHeladera(Heladera heladera) {

     transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(heladera);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar heladera: " + heladera.toString() + " Descripción de fallo: " + e.getMessage());


    }
  }

  @Override
  public void agregarModelo(ModeloHeladera modeloHeladera) {

     transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(modeloHeladera);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar modelo de heladera: " + modeloHeladera.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public List<ModeloHeladera> buscarModelos() {

    Query<ModeloHeladera> query = session.createQuery("from ModeloHeladera", ModeloHeladera.class);
    return query.getResultList();
  }

  @Override
  public Optional<ModeloHeladera> buscarModeloPorId(Long id) {

    Query<ModeloHeladera> query = session.createQuery("from ModeloHeladera where id = :id", ModeloHeladera.class);
    query.setParameter("id", id);
    return query.getResultStream().findFirst();
  }

  @Override
  public Optional<Heladera> buscarPorNombre(String nombre) {

    Query<Heladera> query = session.createQuery("from Heladera where puntoEstrategico.nombre = :nombre", Heladera.class);
    query.setParameter("nombre", nombre);
    return query.getResultStream().findFirst();

  }


  @Override
  public Optional<Heladera> buscarPorId(Long id) {

    Query<Heladera> query = session.createQuery("from Heladera where id = :id", Heladera.class);
    query.setParameter("id", id);
    return query.getResultStream().findFirst();

  }

  @Override
  public List<Heladera> buscarTodas() {
    Query<Heladera> query = session.createQuery("from Heladera", Heladera.class);
    return query.getResultList();

  }

  @Override
  public void eliminarSolicitudesCaducadas() {

     transaction = null;
    try {
      transaction = session.beginTransaction();
      List<Heladera> heladeras = session.createQuery("from Heladera", Heladera.class).getResultList();
      for (Heladera heladera : heladeras) {
        heladera.setSolicitudesDeApertura(heladera.getSolicitudesDeApertura().stream()
            .filter(solicitudDeApertura -> !heladera.caducoTiempoDeEjecucion(solicitudDeApertura))
            .toList());
        session.merge(heladera);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al eliminar solicitudes caducadas. Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public void eliminarHeladeraPorNombre(String nombre) {

     transaction = null;
    try {
      transaction = session.beginTransaction();
      Heladera heladera = session.createQuery("from Heladera where puntoEstrategico.nombre = :nombre", Heladera.class)
          .setParameter("nombre", nombre)
          .getResultStream().findFirst()
          .orElseThrow(() -> new FalloPeticionBaseDeDatosException("Heladera no encontrada: " + nombre));
      session.remove(heladera);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al eliminar heladera: " + nombre + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public List<PuntoHeladera> buscarPuntosHeladera() {

    Query<Heladera> query = session.createQuery("from Heladera", Heladera.class);
    List<Heladera> heladeras = query.getResultList();

    return heladeras.stream()
        .map(Heladera::getPuntoEstrategico)
        .collect(Collectors.toList());
  }
}
