package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.RegistroUsoDeTarjeta;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import java.util.List;
import java.util.Optional;

public interface ITarjetaRepository {

   List<Tarjeta> buscarTodos();

   Optional<Tarjeta> buscarPorId(String id);

   void agregar(Tarjeta tarjeta);

  void agregar(RegistroUsoDeTarjeta registroUsoDeTarjeta);

  List<RegistroUsoDeTarjeta> buscarTodosLosUsosDeTarjetas();




}
