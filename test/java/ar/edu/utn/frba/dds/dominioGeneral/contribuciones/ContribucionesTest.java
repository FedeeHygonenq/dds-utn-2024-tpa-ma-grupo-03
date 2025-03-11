package ar.edu.utn.frba.dds.dominioGeneral.contribuciones;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.BeneficiosPorPuntos;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.hacerseCargoHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContribucionesTest {

  Colaborador colaboradorHumano = Colaborador.builder().tipoColaborador(TipoColaborador.HUMANO).build();
  Colaborador colaboradorJuridico = Colaborador.builder().tipoColaborador(TipoColaborador.JURIDICO).build();

  @Test //Las contribuciones distinguen el tipo de colaborador
  public void aceptaColaborador() {
    List<Contribucion> listaContribuciones = new ArrayList<>();

// Crear BeneficiosPorPuntos y establecer colaborador
    BeneficiosPorPuntos beneficiosPorPuntos = BeneficiosPorPuntos.builder().build();
    beneficiosPorPuntos.setColaborador(colaboradorJuridico);
    listaContribuciones.add(beneficiosPorPuntos);

// Crear HacerseCargoDeHeladera y establecer colaborador
    HacerseCargoDeHeladera hacerseCargoDeHeladera = HacerseCargoDeHeladera.builder().build();
    hacerseCargoDeHeladera.setColaborador(colaboradorJuridico);
    listaContribuciones.add(hacerseCargoDeHeladera);

// Crear DistribucionDeViandas y establecer colaborador
    DistribucionDeViandas distribucionDeViandas = DistribucionDeViandas.builder().build();
    distribucionDeViandas.setColaborador(colaboradorHumano);
    listaContribuciones.add(distribucionDeViandas);

// Crear DonacionDeDinero y establecer colaborador
    DonacionDeDinero donacionDeDinero = DonacionDeDinero.builder().build();
    donacionDeDinero.setColaborador(colaboradorJuridico);
    listaContribuciones.add(donacionDeDinero);

// Crear DonacionDeViandas y establecer colaborador
    DonacionDeViandas donacionDeViandas = DonacionDeViandas.builder().build();
    donacionDeViandas.setColaborador(colaboradorHumano);
    listaContribuciones.add(donacionDeViandas);

// Crear RegistroDePersonaVulnerable y establecer colaborador
    RegistroDePersonaVulnerable registroDePersonaVulnerable = RegistroDePersonaVulnerable.builder().build();
    registroDePersonaVulnerable.setColaborador(colaboradorHumano);
    listaContribuciones.add(registroDePersonaVulnerable);


    Assertions.assertTrue(listaContribuciones.stream().allMatch(Contribucion::contribuidorAceptado));
  }

  @Test //Las contribuciones distinguen el tipo de colaborador
  public void rechazarColaborador() {
    List<Contribucion> listaContribuciones = new ArrayList<>();

// Crear BeneficiosPorPuntos y establecer colaborador
    BeneficiosPorPuntos beneficiosPorPuntos = BeneficiosPorPuntos.builder().build();
    beneficiosPorPuntos.setColaborador(colaboradorHumano);
    listaContribuciones.add(beneficiosPorPuntos);

// Crear HacerseCargoDeHeladera y establecer colaborador
    HacerseCargoDeHeladera hacerseCargoDeHeladera = HacerseCargoDeHeladera.builder().build();
    hacerseCargoDeHeladera.setColaborador(colaboradorHumano);
    listaContribuciones.add(hacerseCargoDeHeladera);

// Crear DistribucionDeViandas y establecer colaborador
    DistribucionDeViandas distribucionDeViandas = DistribucionDeViandas.builder().build();
    distribucionDeViandas.setColaborador(colaboradorJuridico);
    listaContribuciones.add(distribucionDeViandas);

// Crear DonacionDeViandas y establecer colaborador
    DonacionDeViandas donacionDeViandas = DonacionDeViandas.builder().build();
    donacionDeViandas.setColaborador(colaboradorJuridico);
    listaContribuciones.add(donacionDeViandas);

// Crear RegistroDePersonaVulnerable y establecer colaborador
    RegistroDePersonaVulnerable registroDePersonaVulnerable = RegistroDePersonaVulnerable.builder().build();
    registroDePersonaVulnerable.setColaborador(colaboradorJuridico);
    listaContribuciones.add(registroDePersonaVulnerable);

    Assertions.assertTrue(listaContribuciones.stream().noneMatch(Contribucion::contribuidorAceptado));
  }


}
