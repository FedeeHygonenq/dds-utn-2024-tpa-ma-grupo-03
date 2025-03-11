package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.MotivoApertura;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.apertura.RegistroApertura;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IAperturasRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class AperturasRepository implements IAperturasRepository {

  @Override
  public List<RegistroApertura> buscarTodas() {
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
      Query<RegistroApertura> query = session.createQuery("from RegistroApertura", RegistroApertura.class);
      return query.getResultList();
    } finally {
      session.close();
    }
  }

  @Override
  public void agregar(RegistroApertura registro) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.persist(registro); // Utiliza persist para agregar una nueva entidad
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar registro: " + registro.toString() + " Descripción de fallo: " + e.getMessage());
    } finally {
      session.close();
    }
  }

  @Override
  public List<RegistroApertura> buscarPorFecha(LocalDateTime fecha) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
      Query<RegistroApertura> query = session.createQuery("from RegistroApertura where fecha = :fecha", RegistroApertura.class);
      query.setParameter("fecha", fecha);
      return query.getResultList();
    } finally {
      session.close();
    }
  }

  @Override
  public List<RegistroApertura> buscarPorMotivoApertura(MotivoApertura motivo) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
      Query<RegistroApertura> query = session.createQuery("from RegistroApertura where motivo = :motivo", RegistroApertura.class);
      query.setParameter("motivo", motivo);
      return query.getResultList();
    } finally {
      session.close();
    }
  }
}
