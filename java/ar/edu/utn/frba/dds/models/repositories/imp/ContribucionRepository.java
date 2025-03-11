package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.Contribucion;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IContribucionRepository;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class ContribucionRepository implements IContribucionRepository {
  List<String> codigoDeContribuciones;
  Session session = HibernateUtil.getSessionFactory().openSession();

  public ContribucionRepository() {
    this.codigoDeContribuciones = new ArrayList<>();
  }
  @Override
  public List<Contribucion> obtenerContribuciones() {


    Query<Contribucion> query = session.createQuery("from Contribucion", Contribucion.class);
    return query.getResultList();

  }

  @Override
  public void agreagarContribucion(Contribucion contribucion) {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(contribucion);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar la contribución: " + e.getMessage());
    }
  }

  @Override
  public Optional<Contribucion> obtenerContribucionPorId(Long id) {


    Query<Contribucion> query = session.createQuery("from Contribucion where id = :id", Contribucion.class);
    query.setParameter("id", id);
    return query.getResultStream().findFirst();

  }

  @Override
  public void agregarCodigo(String codigo) {
    codigoDeContribuciones.add(codigo);
  }

  @Override
  public void eliminarCodigo(String codigo) {
    codigoDeContribuciones.remove(codigo);
  }

  @Override
  public Boolean existeCodigo(String codigo) {
    return codigoDeContribuciones.contains(codigo);
  }
}
