package ar.edu.utn.frba.dds.dominioGeneral.suscripciones;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sub_faltan_n_viandas")
public class SubFaltanNViandas implements Suscripcion{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "colaborador_x_sub_faltan_viandas",
        joinColumns = @JoinColumn(name = "id_sub_faltan_n_viandas", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_colaborador", referencedColumnName = "id")
    )
    private List<Colaborador> colaboradoresSuscriptos;

    @Embedded
    private Area area;

    @Column(name = "numeroViandas", columnDefinition = "INTEGER", nullable = false)
    private Integer numeroViandas;

    @OneToMany
    @JoinColumn(name = "registro_mensaje", referencedColumnName = "id")
    private List<RegistroMensaje> registroMensajes;

    public SubFaltanNViandas(Area area, Integer cantViandas) {
        this.area = area;
        this.numeroViandas = cantViandas;
        this.colaboradoresSuscriptos = new ArrayList<>();
        this.registroMensajes = new ArrayList<>();
    }

    private String generarMensaje(Heladera heladera,  List<Heladera> heladerasCercanas){
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("A la heladera ")
                .append(heladera.getPuntoEstrategico().getNombre())
                .append(String.format(" le faltan %d viandas para llenarse o menos, puede distribuir viandas a la heladera ", numeroViandas));

        Heladera heladeraRecomendada = heladeraRecomendada(heladerasCercanas);
        if(heladeraRecomendada == null){
            mensaje.append(" no hay heladeras disponibles ahora mismo");
        }
        else{
            mensaje.append(heladeraRecomendada.getPuntoEstrategico().getNombre())
                    .append(" que tiene una capacidad en viandas restante de ")
                    .append(heladeraRecomendada.capacidadDisponible());
        }

        return mensaje.toString();
    }
    @Override
    public void notificar(Heladera heladera, List<Heladera> heladerasCercanas) {
        String mensaje = generarMensaje(heladera, heladerasCercanas);
        Notificacion notificacion = Notificacion.builder()
                .asunto("Faltan pocas viandas para que se llene la heladera")
                .mensaje(mensaje).build();
        ServiceLocator.notificador().enviar(colaboradoresSuscriptos,notificacion);
        registrarMensaje(mensaje);
    }

    public void registrarMensaje(String mensaje) {
        RegistroMensaje registro = new RegistroMensaje(mensaje,colaboradoresSuscriptos);
        registroMensajes.add(registro);
    }

    @Override
    public void agregar(Colaborador colaborador) {
        colaboradoresSuscriptos.add(colaborador);
    }

    @Override
    public void eliminar(Colaborador colaborador) {
        colaboradoresSuscriptos.remove(colaborador);
    }

    private Heladera heladeraRecomendada(List<Heladera> heladeras){
        heladeras.sort((h1, h2) -> h2.capacidadOcupada().compareTo(h1.capacidadOcupada()));
        List<Heladera> heladerasRecomendadas = heladeras
                .stream()
                .filter(h -> h.capacidadDisponible() >= 1)
                .toList();
        return heladerasRecomendadas.isEmpty() ? null : heladerasRecomendadas.get(0);
    }

    @Override
    public Boolean estaSuscrito(Colaborador colaborador) {
        return colaboradoresSuscriptos.contains(colaborador);
    }
}
