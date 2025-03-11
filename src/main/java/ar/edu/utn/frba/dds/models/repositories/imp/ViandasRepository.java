package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IViandasRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ViandasRepository implements IViandasRepository {
  Session session = HibernateUtil.getSessionFactory().openSession();

  @Override
  public void eliminarTodos() {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      Query<?> query = session.createQuery("delete from Vianda");
      query.executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al eliminar todas las viandas. Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public void guardarVianda(Vianda vianda) {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(vianda);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar vianda: " + vianda.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public void eliminarVianda(Vianda vianda) {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.remove(vianda);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al eliminar vianda: " + vianda.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public void guardarViandas(List<Vianda> viandas) {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      for (Vianda vianda : viandas) {
        session.persist(vianda);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar viandas. Descripción de fallo: " + e.getMessage());

    }
  }

  @Override
  public List<Vianda> buscarTodas() {
    Query<Vianda> query = session.createQuery("from Vianda ", Vianda.class);
    return query.getResultList();
  }

  @Override
  public Optional<Vianda> buscarPorId(Long id) {

    Query<Vianda> query = session.createQuery("from Vianda where id = :id", Vianda.class);
    query.setParameter("id", id);
    return query.getResultStream().findFirst();
  }

}
