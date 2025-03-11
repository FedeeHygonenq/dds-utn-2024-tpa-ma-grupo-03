package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.hacerseCargoHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FalloPeticionBaseDeDatosException;
import ar.edu.utn.frba.dds.models.repositories.HibernateUtil;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IColaboradorRepository;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class ColaboradorRepository implements IColaboradorRepository {
  Session session = HibernateUtil.getSessionFactory().openSession();
  Transaction transaction = null;

  @Override
  public List<Colaborador> buscarTodos() {
      Query<Colaborador> query = session.createQuery("from Colaborador", Colaborador.class);
      return query.getResultList();
  }

  @Override
  public Optional<Colaborador> buscarPorId(Long id) {

    try {
      Colaborador colaborador = session.get(Colaborador.class, id);
      return Optional.ofNullable(colaborador);
    } finally {
      ;
    }
  }


  @Override
  public Optional<Colaborador> buscarPorNombreDeUsuario(String nombreUsuario) {

      Query<Colaborador> query = session.createQuery("from Colaborador where usuario.usuario = :usuario", Colaborador.class);
      query.setParameter("usuario", nombreUsuario);
      return query.getResultStream().findFirst();

  }

  @Override
  public Optional<List<Heladera>> buscarHeladerasPorColaborador(Long idColaborador) {
      Query<HacerseCargoDeHeladera> query = session.createQuery(
          "from HacerseCargoDeHeladera where colaborador.id = :idColaborador", HacerseCargoDeHeladera.class);
      query.setParameter("idColaborador", idColaborador);
      List<HacerseCargoDeHeladera> contribuciones = query.getResultList();

      List<Heladera> heladeras = contribuciones.stream()
          .map(HacerseCargoDeHeladera::getHeladera)
          .collect(Collectors.toList());

      return Optional.of(heladeras);

  }


  @Override
  public Optional<Colaborador> buscarPorNumeroDeDocumento(String numeroDeDocumento) {


      Query<Colaborador> query = session.createQuery("from Colaborador where numeroDeDocumento = :numeroDeDocumento", Colaborador.class);
      query.setParameter("numeroDeDocumento", numeroDeDocumento);
      return query.getResultStream().findFirst();
  }

    @Override
    public Optional<Colaborador> buscarPorTarjeta(String tarjetaId) {
        Query<Colaborador> query = session.createQuery(
                "select c from Colaborador c join c.tarjetas t where t.id = :tarjetaId",
                Colaborador.class
        );
        query.setParameter("tarjetaId", tarjetaId);
        return query.getResultStream().findFirst();
    }


    @Override
  public void guardarOActualizar(Colaborador colaborador) {

     transaction = null;
    try {
      transaction = session.beginTransaction();
      session.saveOrUpdate(colaborador);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }

      throw new FalloPeticionBaseDeDatosException("Fallo al agregar colaborador: " + colaborador.toString() + " Descripción de fallo: " + e.getMessage());
    }
  }

  @Override
  public void eliminarTodos() {

     transaction = null;
    try {
      transaction = session.beginTransaction();

      // Delete related entities first
      session.createQuery("delete from Tarjeta").executeUpdate();
      session.createQuery("delete from Contacto").executeUpdate();
      session.createQuery("delete from Contribucion ").executeUpdate();
      session.createQuery("delete from Canje").executeUpdate();

      // Delete Colaborador entities
      session.createQuery("delete from Colaborador").executeUpdate();

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw new FalloPeticionBaseDeDatosException("Fallo al eliminar todos los colaboradores. Descripción de fallo: " + e.getMessage());
    }
  }

}
