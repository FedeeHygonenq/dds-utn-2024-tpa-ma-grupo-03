package ar.edu.utn.frba.dds.dominioGeneral.contribuciones.benificios;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;

public class BeneficiosTest {

  Colaborador colaboradorHumano = Colaborador.builder().tipoColaborador(TipoColaborador.HUMANO).build();
  Colaborador colaboradorJuridico = Colaborador.builder().tipoColaborador(TipoColaborador.HUMANO).build();
}
