package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IUsuarioRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UsuarioRepository implements IUsuarioRepository {
  Session session = HibernateUtil.getSessionFactory().openSession();

  @Override
  public List<Usuario> buscarTodos() {
    Query<Usuario> query = session.createQuery("from Usuario", Usuario.class);
    return query.getResultList();

  }

  @Override
  public Optional<Usuario> buscarPorId(String nombre) {


      Usuario usuario = session.get(Usuario.class, nombre);
      return Optional.ofNullable(usuario);

  }

  @Override
  public void agregar(Usuario usuario) {

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(usuario);  // saveOrUpdate inserta o actualiza
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar usuario: " + usuario.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }
}
