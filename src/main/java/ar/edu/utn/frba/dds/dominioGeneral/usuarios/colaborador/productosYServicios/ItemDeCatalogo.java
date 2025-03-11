package ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.productosYServicios;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Setter
@Table(name = "item_de_catalogo")
public class ItemDeCatalogo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "descripcion", columnDefinition = "VARCHAR(255)")
    private String descripcion;

    @Column(name = "precio", columnDefinition = "DECIMAL(18,2)", nullable = false)
    private Float precio;

    @Column(name = "marca", columnDefinition = "VARCHAR(255)")
    private String marca;

    @Column(name = "cantidad")
    private Integer cantidad;

    public ItemDeCatalogo(String descripcion, Float precio, Integer cantidad) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Float obtenerPrecioTotal() {
        return precio * cantidad;
    }
}
