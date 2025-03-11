package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroTemperatura;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IRegistrosSensor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RegistrosSensorRepository implements IRegistrosSensor {
    Session session = HibernateUtil.getSessionFactory().openSession();
    @Override
    public void agregarRegistroMovimiento(RegistroMovimiento registroMovimiento) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(registroMovimiento); // Utiliza persist para agregar una nueva entidad
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new FalloPeticionBaseDeDatosException("Fallo al agregar registro de movimiento: " + registroMovimiento.toString() + " Descripción de fallo: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void agregarRegistroTemperatura(RegistroTemperatura registroTemperatura) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(registroTemperatura); // Utiliza persist para agregar una nueva entidad
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new FalloPeticionBaseDeDatosException("Fallo al agregar registro de temperatura: " + registroTemperatura.toString() + " Descripción de fallo: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List<RegistroMovimiento> buscarRegistrosMovimiento() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<RegistroMovimiento> query = session.createQuery("from RegistroMovimiento", RegistroMovimiento.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    @Override
    public List<RegistroTemperatura> buscarRegistrosTemperatura() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<RegistroTemperatura> query = session.createQuery("from RegistroTemperatura", RegistroTemperatura.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}
