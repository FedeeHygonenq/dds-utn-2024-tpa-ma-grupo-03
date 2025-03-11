package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.productosYServicios.ItemDeCatalogo;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IItemDeCatalogoRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ItemDeCatalogoRepository implements IItemDeCatalogoRepository {

  public List<ItemDeCatalogo> buscarTodos()
  {
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
      Query<ItemDeCatalogo> query = session.createQuery("from ItemDeCatalogo", ItemDeCatalogo.class);
      return query.getResultList();
    } finally {
      session.close();
    }
  }

  public Optional<ItemDeCatalogo> buscarPorId(Long id)
  {
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
      ItemDeCatalogo itemDeCatalogo = session.get(ItemDeCatalogo.class, id);
      return Optional.ofNullable(itemDeCatalogo);
    } finally {
      session.close();
    }
  }

  public void agregar(ItemDeCatalogo itemDeCatalogo)
  {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(itemDeCatalogo);  // saveOrUpdate inserta o actualiza
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al agregar item de catalogo: " + itemDeCatalogo.toString() + " Descripción de fallo: " + e.getMessage());
    } finally {
      session.close();
    }

  }
}
