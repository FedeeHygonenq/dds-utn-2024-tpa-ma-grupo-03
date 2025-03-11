package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.SubDesperfectoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.SubFaltanNViandas;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.SubQuedanNViandas;
import ar.edu.utn.frba.dds.dominioGeneral.suscripciones.Suscripcion;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.ISuscripcionRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SuscripcionRepository implements ISuscripcionRepository {
  Session session = HibernateUtil.getSessionFactory().openSession();

  @Override
  public List<Suscripcion> buscarTodas() {


      List<Suscripcion> todasLasSuscripciones = new ArrayList<>();
      todasLasSuscripciones.addAll(session.createQuery("from SubDesperfectoHeladera", SubDesperfectoHeladera.class).getResultList());
      todasLasSuscripciones.addAll(session.createQuery("from SubFaltanNViandas", SubFaltanNViandas.class).getResultList());
      todasLasSuscripciones.addAll(session.createQuery("from SubQuedanNViandas", SubQuedanNViandas.class).getResultList());
      return todasLasSuscripciones;

  }

  @Override
  public void agregar(Suscripcion suscripcion) {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(suscripcion);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar suscripción: " + suscripcion.toString() + " Descripción de fallo: " + e.getMessage());
    }

  }

  @Override
  public List<SubDesperfectoHeladera> buscarTodasSubDesperfectoHeladera() {

      return session.createQuery("from SubDesperfectoHeladera", SubDesperfectoHeladera.class).getResultList();

  }

  @Override
  public List<SubFaltanNViandas> buscarTodasSubFaltanNViandas() {

      return session.createQuery("from SubFaltanNViandas", SubFaltanNViandas.class).getResultList();

  }

  @Override
  public List<SubQuedanNViandas> buscarTodasSubQuedanNViandas() {

      return session.createQuery("from SubQuedanNViandas", SubQuedanNViandas.class).getResultList();

  }

  @Override
  public Optional<SubFaltanNViandas> buscarSubFaltanNViandasPorAreaYViandas(Area area, Integer cantViandas) {
      Query<SubFaltanNViandas> query = session.createQuery("from SubFaltanNViandas where area = :area and numeroViandas = :cantViandas", SubFaltanNViandas.class)
          .setParameter("area", area)
          .setParameter("cantViandas", cantViandas);
      return query.getResultStream().findFirst();

  }

  @Override
  public Optional<SubQuedanNViandas> buscarSubQuedanNViandasPorAreaYViandas(Area area, Integer cantViandas) {

      Query<SubQuedanNViandas> query = session.createQuery("from SubQuedanNViandas where area = :area and numeroViandas = :cantViandas", SubQuedanNViandas.class)
          .setParameter("area", area)
          .setParameter("cantViandas", cantViandas);
      return query.getResultStream().findFirst();

  }

  @Override
  public Optional<SubDesperfectoHeladera> buscarSubDesperfectoHeladeraPorArea(Area area) {
    ;

      Query<SubDesperfectoHeladera> query = session.createQuery("from SubDesperfectoHeladera where area = :area", SubDesperfectoHeladera.class)
          .setParameter("area", area);
      return query.getResultStream().findFirst();

  }
}
