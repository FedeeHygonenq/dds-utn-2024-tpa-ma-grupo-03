package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IOfertaRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class OfertaRepository implements IOfertaRepository {
  Session session = HibernateUtil.getSessionFactory().openSession();

  public List<Oferta> buscarTodos() {


    Query<Oferta> query = session.createQuery("from Oferta", Oferta.class);
    return query.getResultList();

  }

  public Optional<Oferta> buscarPorId(Long id) {


    Oferta oferta = session.get(Oferta.class, id);
    return Optional.ofNullable(oferta);

  }

  public void agregar(Oferta oferta) {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(oferta);  // saveOrUpdate inserta o actualiza
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar oferta: " + oferta.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }


}
