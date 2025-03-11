package ar.edu.utn.frba.dds.dominioGeneral.contribuciones.hacerseCargoHeladera;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.Contribucion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.TipoColaborador;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.services.externos.puntoRecomendado.AdapterPuntosRecomendados;
import ar.edu.utn.frba.dds.services.externos.puntoRecomendado.IAdapterPuntosRecomendado;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contribucion_hacerse_cargo_de_heladera")
public class HacerseCargoDeHeladera extends Contribucion {

    @OneToOne
    @JoinColumn(name = "id_heladera", referencedColumnName = "id", nullable = false)
    @Cascade(CascadeType.ALL)
    private Heladera heladera;

    @Column(name = "fecha", columnDefinition = "DATE", nullable = false)
    private LocalDate fechaCargo;

    @Transient
    private IAdapterPuntosRecomendado adapterPuntosRecomendado;


    public HacerseCargoDeHeladera(Heladera heladera, Colaborador colaborador) {
      this.heladera = heladera;
      this.fechaCargo = LocalDate.now();
      this.adapterPuntosRecomendado = new AdapterPuntosRecomendados();
      this.setColaborador(colaborador);
    }

    public Boolean heladeraActiva() {
        return this.heladera.getEstado() == EstadoHeladera.ACTIVA;
    }

    public Integer mesesActiva() { //CONSULTAR A LAS CHICAS
        return this.heladera.calcularMesesActiva(this.fechaCargo);
    }

    //Si no se usa la API de puntos entonces se tiene que ingresar el nombre que va a tener el punto y se usa la direccion del contribuidor
    public void colocarEnPunto(String nombrePunto) {
        PuntoHeladera puntoEstrategico = PuntoHeladera.builder().nombre(nombrePunto).direccion(this.getColaborador().getDireccion()).build();
        heladera.setPuntoEstrategico(puntoEstrategico);
    }

    public void colocarEnPunto(PuntoHeladera puntoHeladera) {
        this.heladera.setPuntoEstrategico(puntoHeladera);
    }

    public List<PuntoHeladera> obtenerPuntosRecomendados(Area area) throws IOException {
        return this.adapterPuntosRecomendado.obtenerPuntos((area));
    }

    public Boolean contribuidorAceptado() {
            return this.getColaborador().getTipoColaborador() == TipoColaborador.JURIDICO;
    }

    public Double multiplicador() {

        return LectorProperties.getDoublePropertie("hacerseCargoDeHeladera");
    }

    @Override
    public Double puntos(Colaborador colaborador)
    {
     return this.mesesActiva() * this.multiplicador();


    }
}
