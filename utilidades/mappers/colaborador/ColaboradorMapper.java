package ar.edu.utn.frba.dds.utilidades.mappers.colaborador;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.Controllers.dtos.ColaboradorDTOOutAPI;

import java.util.List;

public class ColaboradorMapper {

  public static ColaboradorDTOOutAPI mapColaboradorAColaboradorDTOUOutAPI(Colaborador colaborador) {

    String contacto = colaborador.getMediosDeComunicacion().get(0).getValor();
    Double puntos = colaborador.puntosDisponiblesEnBilletera();
    Integer cantidadDeColaboraciones = colaborador.getContribuciones().size();

    return new ColaboradorDTOOutAPI(colaborador.getNombre(), colaborador.getApellido(),contacto,puntos,cantidadDeColaboraciones);
  }

  public static List<ColaboradorDTOOutAPI> mapColaboradoresAColaboradoresDTOOutAPI(List<Colaborador> colaboradores) {
    return colaboradores.stream().map(ColaboradorMapper::mapColaboradorAColaboradorDTOUOutAPI).toList();
  }
}
