package ar.edu.utn.frba.dds.db.reposTest;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoDocumento;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos.BilleteraDePuntos;
import ar.edu.utn.frba.dds.models.repositories.imp.ColaboradorRepository;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RepoColaboradorTest {


  private ColaboradorRepository colaboradorRepository = ServiceLocator.colaboradorRepository();



  @AfterEach
  public void tearDown() {

  }

  private void cargarDatosIniciales() {

    BilleteraDePuntos billeteraDePuntos = new BilleteraDePuntos();
    billeteraDePuntos.setPuntosActuales(0.0);
    billeteraDePuntos.setPuntosGastados(0.0);
    billeteraDePuntos.setHistorialDePuntosGastados(new ArrayList<>());


      Colaborador colaborador1 = new Colaborador();
      colaborador1.setNombre("Juan");
      colaborador1.setApellido("Perez");
      colaborador1.setTipoColaborador(TipoColaborador.HUMANO);
      colaborador1.setTipoDocumento(TipoDocumento.DNI);
      colaborador1.setNumeroDeDocumento("12345678");



      Colaborador colaborador2 = new Colaborador();
      colaborador2.setNombre("Maria");
      colaborador2.setApellido("Gomez");
      colaborador2.setTipoColaborador(TipoColaborador.HUMANO);
      colaborador2.setTipoDocumento(TipoDocumento.DNI);
      colaborador2.setNumeroDeDocumento("87654321");

      colaboradorRepository.guardarOActualizar(colaborador1);
      colaboradorRepository.guardarOActualizar(colaborador2);

  }

  @Test
  public void testGuardarColaborador() {
    Assertions.assertDoesNotThrow(this::cargarDatosIniciales);
  }


  @Test
  public void recuperarColaboradores() {
    Colaborador colaborador1 = new Colaborador();
    colaborador1.setNombre("Juan");
    colaborador1.setApellido("Perez");
    colaborador1.setTipoColaborador(TipoColaborador.HUMANO);
    colaborador1.setTipoDocumento(TipoDocumento.DNI);
    colaborador1.setNumeroDeDocumento("12345678");



    Colaborador colaborador2 = new Colaborador();
    colaborador2.setNombre("Maria");
    colaborador2.setApellido("Gomez");
    colaborador2.setTipoColaborador(TipoColaborador.HUMANO);
    colaborador2.setTipoDocumento(TipoDocumento.DNI);
    colaborador2.setNumeroDeDocumento("87654321");

    cargarDatosIniciales();
    List<Colaborador> colaboradoresRecuperados = colaboradorRepository.buscarTodos();
    Assertions.assertEquals(colaborador1.getNumeroDeDocumento(), colaboradoresRecuperados.get(0).getNumeroDeDocumento());
    Assertions.assertEquals(colaborador2.getNumeroDeDocumento(), colaboradoresRecuperados.get(1).getNumeroDeDocumento());
  }

  @Test
  public void recuperarColaboradorPorDocumento() {
    Colaborador colaborador1 = new Colaborador();
    colaborador1.setNombre("Juan");
    colaborador1.setApellido("Perez");
    colaborador1.setTipoColaborador(TipoColaborador.HUMANO);
    colaborador1.setTipoDocumento(TipoDocumento.DNI);
    colaborador1.setNumeroDeDocumento("12345678");

    cargarDatosIniciales();
    Optional<Colaborador> colaboradorRecuperado = colaboradorRepository.buscarPorNumeroDeDocumento("12345678");
    Assertions.assertEquals(colaborador1.getNumeroDeDocumento(), colaboradorRecuperado.get().getNumeroDeDocumento());
  }
}
