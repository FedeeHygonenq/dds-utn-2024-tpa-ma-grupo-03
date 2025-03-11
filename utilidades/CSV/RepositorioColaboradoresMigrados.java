package ar.edu.utn.frba.dds.utilidades.CSV;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import java.util.ArrayList;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class RepositorioColaboradoresMigrados {
    private List<Colaborador> colaboradores;

    public RepositorioColaboradoresMigrados(){
        this.colaboradores = new ArrayList<>();
    }
    public Colaborador colaboradorSegunID(String id){
        Optional<Colaborador> optionalColab = this.colaboradores.stream()
                .filter(colaborador -> Objects.equals(colaborador.getNumeroDeDocumento(), id))
                .findFirst();

        Colaborador colab = optionalColab.orElse(null);
        return colab;
    }

    public void agregarColaborador(Colaborador colaborador){
        this.colaboradores.add(colaborador);
    }
    
}
