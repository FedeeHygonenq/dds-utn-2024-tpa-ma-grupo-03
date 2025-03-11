package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.PersonaEnSituacionVulnerable;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IPersonaVulnerableRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class PersonaVulnerableRepository implements IPersonaVulnerableRepository {
  Session session = HibernateUtil.getSessionFactory().openSession();
  Transaction transaction = null;
  @Override
  public List<PersonaEnSituacionVulnerable> buscarTodos() {

    Query<PersonaEnSituacionVulnerable> query = session.createQuery("from PersonaEnSituacionVulnerable", PersonaEnSituacionVulnerable.class);
    return query.getResultList();

  }

  @Override
  public void guardarPersonaVulnerable(PersonaEnSituacionVulnerable personaEnSituacionVulnerable) {

     transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(personaEnSituacionVulnerable);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar persona vulnerable: " + personaEnSituacionVulnerable.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public Optional<PersonaEnSituacionVulnerable> buscarPorId(Integer id) {


    PersonaEnSituacionVulnerable persona = session.get(PersonaEnSituacionVulnerable.class, id);
    return Optional.ofNullable(persona);
  }


  @Override
  public Optional<PersonaEnSituacionVulnerable> buscarPorDocumento(String documento) {


    Query<PersonaEnSituacionVulnerable> query = session.createQuery("from PersonaEnSituacionVulnerable where numeroDeDocumento = :numeroDeDocumento", PersonaEnSituacionVulnerable.class);
    query.setParameter("numeroDeDocumento", documento);
    return query.getResultStream().findFirst();
  }

  @Override
  public Optional<PersonaEnSituacionVulnerable> buscarPorTarjetaId(String tarjeta) {

    Query<PersonaEnSituacionVulnerable> query = session.createQuery("from PersonaEnSituacionVulnerable where tarjeta.id = :tarjeta", PersonaEnSituacionVulnerable.class);
    query.setParameter("tarjeta", tarjeta);
    return query.getResultStream().findFirst();
  }
}

