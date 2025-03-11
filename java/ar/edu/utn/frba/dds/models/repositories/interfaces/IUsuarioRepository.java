package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {

     List<Usuario> buscarTodos();
     Optional<Usuario> buscarPorId(String nombre);
     void agregar(Usuario usuario);

}
