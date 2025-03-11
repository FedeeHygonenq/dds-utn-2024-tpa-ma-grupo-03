package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.RegistroUsoDeTarjeta;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.ITarjetaRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TarjetaRepository implements ITarjetaRepository {
  Session session = HibernateUtil.getSessionFactory().openSession();

  @Override
  public List<Tarjeta> buscarTodos() {

    Query<Tarjeta> query = session.createQuery("from Tarjeta", Tarjeta.class);
    return query.getResultList();

  }

  @Override
  public Optional<Tarjeta> buscarPorId(String id) {


    return session.createQuery("from Tarjeta where id = :id", Tarjeta.class)
        .setParameter("id", id)
        .getResultStream()
        .findFirst();

  }

  @Override
  public void agregar(Tarjeta tarjeta) {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(tarjeta); // Se usa persist para agregar la entidad
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar tarjeta: " + tarjeta.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public void agregar(RegistroUsoDeTarjeta registroUsoDeTarjeta) {
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(registroUsoDeTarjeta); // Utiliza persist para agregar una nueva entidad
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar registro: " + registroUsoDeTarjeta.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public List<RegistroUsoDeTarjeta> buscarTodosLosUsosDeTarjetas() {

    Query<RegistroUsoDeTarjeta> query = session.createQuery("from RegistroUsoDeTarjeta", RegistroUsoDeTarjeta.class);
    return query.getResultList();
  }

}
