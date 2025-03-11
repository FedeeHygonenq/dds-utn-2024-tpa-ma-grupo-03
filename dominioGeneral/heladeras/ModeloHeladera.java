package ar.edu.utn.frba.dds.dominioGeneral.heladeras;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "modelos_heladeras")
public class ModeloHeladera {

   @Id
   @GeneratedValue
   private Long id;
   @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
   private String nombre;
   @Column(name = "temperaturaMaxima", columnDefinition = "DECIMAL(5,2)")
   private Float temperaturaMaxima;
   @Column(name = "temperaturaMinima", columnDefinition = "DECIMAL(5,2)")
   private Float temperaturaMinima;
   @Column(name = "capacidadEnViandas", columnDefinition = "INTEGER")
   private Integer capacidadEnViandas;

   public ModeloHeladera(String nombre,Float temperaturaMaxima, Float temperaturaMinima, Integer capacidadEnViandas) {
      this.nombre = nombre;
      this.temperaturaMaxima = temperaturaMaxima;
      this.temperaturaMinima = temperaturaMinima;
      this.capacidadEnViandas = capacidadEnViandas;
   }

}
