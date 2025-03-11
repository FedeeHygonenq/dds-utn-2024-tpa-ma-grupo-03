package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.SolicitudDeApertura;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.ISolicitudAperturaRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SolicitudAperturaRepository implements ISolicitudAperturaRepository {

  @Override
  public void agregar(SolicitudDeApertura registro) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.persist(registro);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new RuntimeException("Fallo al agregar solicitud de apertura: " + registro.toString() + " Descripción de fallo: " + e.getMessage(), e);
    } finally {
      session.close();
    }
  }

  @Override
  public List<SolicitudDeApertura> buscarTodos() {
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
      Query<SolicitudDeApertura> query = session.createQuery("from SolicitudDeApertura", SolicitudDeApertura.class);
      return query.getResultList();
    } finally {
      session.close();
    }
  }
}
