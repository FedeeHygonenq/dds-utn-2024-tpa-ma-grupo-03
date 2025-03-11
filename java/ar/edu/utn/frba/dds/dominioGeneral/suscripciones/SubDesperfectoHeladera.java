package ar.edu.utn.frba.dds.dominioGeneral.suscripciones;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.notificador.Notificacion;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sub_desperfecto_heladera")
public class SubDesperfectoHeladera implements Suscripcion{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "colaborador_x_sub_desperfecto_heladera",
        joinColumns = @JoinColumn(name = "id_sub_desperfecto_heladera", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_colaborador", referencedColumnName = "id")
    )
    private List<Colaborador> colaboradoresSuscriptos;
    @Embedded
    private Area area;

    @OneToMany
    @JoinColumn(name = "registro_mensaje", referencedColumnName = "id")
    private List<RegistroMensaje> registroMensajes;

    public SubDesperfectoHeladera(Area area) {
        this.area = area;
        this.colaboradoresSuscriptos = new ArrayList<>();
        this.registroMensajes = new ArrayList<>();
    }

    private String generarMensaje(Heladera heladera,List<Heladera> heladerasCercanas) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("La heladera ")
                .append(heladera.getPuntoEstrategico().getNombre())
                .append(" ha sufrido un desperfecto, puede llevar las viandas a las siguientes heladeras cercanas:\n");
        List<Heladera> heladerasRecomendadas = heladerasRecomendadas(heladerasCercanas);
        if(heladerasRecomendadas == null){
            mensaje.append("No hay heladeras disponibles en este momento");
        }
        else {
            for (Heladera heladeraCercana : heladerasRecomendadas) {
                mensaje.append("\t-")
                        .append(heladeraCercana.getPuntoEstrategico().getNombre())
                        .append(" con una capacidad en viandas de ")
                        .append(heladeraCercana.capacidadDisponible())
                        .append("\n");
            }
        }

        return mensaje.toString();
    }
    @Override
    public void notificar(Heladera heladera, List<Heladera> heladerasCercanas) {
        String mensaje = generarMensaje(heladera, heladerasCercanas);
        Notificacion notificacion = Notificacion.builder()
                .asunto("Desperfecto en heladera")
                .mensaje(mensaje).build();
        ServiceLocator.notificador().enviar(colaboradoresSuscriptos,notificacion);
        registrarMensaje(mensaje);
    }

    @Override
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

    @Override
    public Boolean estaSuscrito(Colaborador colaborador) {
        return colaboradoresSuscriptos.contains(colaborador);
    }

    private List<Heladera> heladerasRecomendadas(List<Heladera> heladeras){
        return heladeras
                .stream()
                .filter(heladera -> heladera.capacidadDisponible()>=1)
                .toList();
    }


}
