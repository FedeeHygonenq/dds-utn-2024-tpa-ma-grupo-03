package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IRolRepository;
import ar.edu.utn.frba.dds.server.roles.Rol;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class RolRepository implements IRolRepository {

        public List<Rol> buscarTodos()
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                Query<Rol> query = session.createQuery("from Rol", Rol.class);
                return query.getResultList();
            } finally {
                session.close();
            }
        }
        public Optional<Rol> buscarPorId(Long id)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                Rol rol = session.get(Rol.class, id);
                return Optional.ofNullable(rol);
            } finally {
                session.close();
            }
        }
        public void agregar(Rol rol)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.saveOrUpdate(rol);  // saveOrUpdate inserta o actualiza
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new FalloPeticionBaseDeDatosException("Fallo al agregar rol: " + rol.toString() + " Descripción de fallo: " + e.getMessage());
            } finally {
                session.close();
            }
        }

}
