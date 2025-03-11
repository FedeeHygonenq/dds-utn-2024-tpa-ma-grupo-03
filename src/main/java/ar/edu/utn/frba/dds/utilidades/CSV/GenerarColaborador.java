package ar.edu.utn.frba.dds.utilidades.CSV;

import static ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador.HUMANO;

import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoDocumento;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Contacto;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos.BilleteraDePuntos;
import ar.edu.utn.frba.dds.server.roles.TipoRol;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Builder
public class GenerarColaborador {
  public Colaborador generar(TipoDocumento tipo, String documento, String nombre, String apellido, String mail) {
    Usuario user = new Usuario(documento,ServiceLocator.usuarioService().hashearContrasenia(documento), TipoRol.COLABORADOR_HUMANO);
    return Colaborador.builder().usuario(user).tipoDocumento(tipo).contribuciones(new ArrayList<>())
        .numeroDeDocumento(documento).tipoColaborador(HUMANO).nombre(nombre).apellido(apellido).billeteraDePuntos(new BilleteraDePuntos())
        .mediosDeComunicacion(List.of(new Contacto(ServiceLocator.comunicacionMail(), mail))).build();
  }
}
