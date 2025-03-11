package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.RegistroTecnico;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.ITecnicosRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TecnicosRepository implements ITecnicosRepository {

  Session session = HibernateUtil.getSessionFactory().openSession();

  @Override
  public List<Tecnico> buscarTodos() {

    Query<Tecnico> query = session.createQuery("from Tecnico", Tecnico.class);
    return query.getResultList();

  }

  @Override
  public Optional<Tecnico> buscarPorCuil(String cuil) {

    return session.createQuery("from Tecnico where cuil = :cuil", Tecnico.class)
        .setParameter("cuil", cuil)
        .getResultStream()
        .findFirst();

  }

  @Override
  public Optional<Tecnico> buscarPorId(Long id) {


    return session.createQuery("from Tecnico where id = :id", Tecnico.class)
        .setParameter("id", id)
        .getResultStream()
        .findFirst();
  }

  @Override
  public Optional<Tecnico> traerPorUsuario(String usuario) {


    return session.createQuery("from Tecnico where usuario.usuario = :usuario", Tecnico.class)
        .setParameter("usuario", usuario)
        .getResultStream()
        .findFirst();
  }

  @Override
  public void agregar(Tecnico tecnico) {
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(tecnico); // Se usa persist para agregar la entidad
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar tecnico: " + tecnico.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public void agrearRegistro(RegistroTecnico registroTecnico) {
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(registroTecnico); // Se usa persist para agregar la entidad
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar registro tecnico: " + registroTecnico.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public List<RegistroTecnico> buscarRegistros() {
    return session.createQuery("from RegistroTecnico", RegistroTecnico.class).getResultList();
  }

  @Override
  public List<RegistroTecnico> buscarRegistrosPorTecnico(Tecnico tecnico) {
    return session.createQuery("from RegistroTecnico where tecnico = :tecnico", RegistroTecnico.class)
        .setParameter("tecnico", tecnico)
        .getResultList();
  }

  @Override
  public List<RegistroTecnico> buscarRegistrosPorHeladera(Long heladeraId) {
    return session.createNativeQuery(
            "SELECT * FROM registro_tecnico WHERE id_heladera = :heladeraId",
            RegistroTecnico.class)
        .setParameter("heladeraId", heladeraId)
        .getResultList();
  }
}
