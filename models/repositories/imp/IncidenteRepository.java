package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroIncidente;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IIncidenteRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class IncidenteRepository implements IIncidenteRepository {
  Session session = HibernateUtil.getSessionFactory().openSession();

  @Override
  public List<RegistroIncidente> buscarTodos() {
      Query<RegistroIncidente> query = session.createQuery("from RegistroIncidente", RegistroIncidente.class);
      return query.getResultList();

  }

  @Override
  public void agregarIncidente(RegistroIncidente registroIncidente) {
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.persist(registroIncidente); // Utiliza persist para agregar una nueva entidad
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar incidente: " + registroIncidente.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public List<RegistroIncidente> buscarPorHeladera(Heladera heladera) {
    Query<RegistroIncidente> query = session.createQuery("from RegistroIncidente where heladera = :heladera", RegistroIncidente.class);
    query.setParameter("heladera", heladera);
    return query.getResultList();
  }
}
