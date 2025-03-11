package ar.edu.utn.frba.dds.models.repositories;

import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.Set;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Crear una instancia de Configuration
            Configuration configuration = new Configuration();

            configuration.configure("/META-INF/hibernate.cfg.xml");

            // Obtener valores de las variables de entorno (si existen)
            String dbUrl = System.getenv("MYSQL_URL");
            String dbUser = System.getenv("MYSQLUSER");
            String dbPassword = System.getenv("MYSQLPASSWORD");
            String mode = System.getenv("MODE");

            // Sobrescribir solo si las variables de entorno están definidas
            if (dbUrl != null) {
                configuration.setProperty("hibernate.connection.url", "jdbc:" + dbUrl);
            }

            if (dbUser != null) {
                configuration.setProperty("hibernate.connection.username", dbUser);
            }

            if (dbPassword != null) {
                configuration.setProperty("hibernate.connection.password", dbPassword);
            }

            if (mode != null) {
                configuration.setProperty("hibernate.hbm2ddl.auto", mode);
            }



            // Usar ClassGraph para encontrar las clases anotadas con @Entity
            try (ScanResult scanResult = new ClassGraph()
                    .enableAllInfo()
                    .acceptPackages("ar.edu.utn.frba.dds") // Cambia esto por el paquete base de tus entidades
                    .scan()) {

                Set<Class<?>> entityClasses = scanResult.getAllClasses().stream()
                        .filter(classInfo -> classInfo.hasAnnotation("javax.persistence.Entity"))
                        .map(classInfo -> classInfo.loadClass())
                        .collect(Collectors.toSet());

                // Agregar las clases de entidad encontradas a la configuración de Hibernate
                for (Class<?> entityClass : entityClasses) {
                    configuration.addAnnotatedClass(entityClass);
                }
            }

            // Construir el ServiceRegistry a partir de la configuración
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            // Construir el SessionFactory
            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Error al construir el SessionFactory: " + ex);
        }
    }

    public static void initializeDatabase() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Ejecutar una consulta simple para verificar la conexión
            session.createNativeQuery("SELECT 1").getSingleResult();


            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al inicializar la base de datos", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        } else {
            return buildSessionFactory();
        }
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
