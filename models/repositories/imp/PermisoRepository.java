package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IPermisoRepository;
import ar.edu.utn.frba.dds.server.roles.Permiso;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class PermisoRepository implements IPermisoRepository {


    public List<Permiso> buscarTodos()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Permiso> query = session.createQuery("from Permiso", Permiso.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    public Optional<Permiso> buscarPorId(Long id)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Permiso permiso = session.get(Permiso.class, id);
            return Optional.ofNullable(permiso);
        } finally {
            session.close();
        }
    }
    public void agregar(Permiso permiso)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(permiso);  // saveOrUpdate inserta o actualiza
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new FalloPeticionBaseDeDatosException("Fallo al agregar permiso: " + permiso.toString() + " Descripción de fallo: " + e.getMessage());
        } finally {
            session.close();
        }
    }
}
