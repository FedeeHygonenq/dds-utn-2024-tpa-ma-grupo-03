package ar.edu.utn.frba.dds.dominioGeneral.heladeras;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "viandas")
public class Vianda{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "comida", columnDefinition = "VARCHAR(255)")
    private String comida;

    @Column (name = "fechaDeCaducidad", columnDefinition = "DATE" )
    private LocalDate fechaDeCaducidad;

    @Column (name = "calorias", columnDefinition = "INTEGER")
    private Integer calorias;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "estaEntregada")
    private Boolean estaEntregada;

    
    public Vianda(String comida, LocalDate fechaDeCaducidad, Integer calorias, Double peso) {
        this.comida = comida;
        this.fechaDeCaducidad = fechaDeCaducidad;
        this.calorias = calorias;
        this.peso = peso;
        this.estaEntregada = false;
    }

    public Vianda(String comida, LocalDate fechaDeCaducidad, Integer calorias, Double peso, Boolean estado) {
        this.comida = comida;
        this.fechaDeCaducidad = fechaDeCaducidad;
        this.calorias = calorias;
        this.peso = peso;
        this.estaEntregada = estado;
    }



    public Boolean estaCaducado(){
        return !LocalDate.now().isBefore(fechaDeCaducidad);
    }
}
