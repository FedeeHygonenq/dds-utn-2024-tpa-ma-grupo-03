package ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.productosYServicios.ItemDeCatalogo;
import lombok.*;

import java.util.List;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oferta")
public class Oferta {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(255)")
    private String nombre;

    @Column(name = "rutaImagen", columnDefinition = "VARCHAR(255)")
    private String rutaImagen;

    @Column(name = "nombreImagen", columnDefinition = "VARCHAR(255)")
    private String nombreImagen;

    @ManyToMany
    @JoinTable(
            name = "item_por_oferta",
            joinColumns = @JoinColumn(name = "id_oferta", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_item", referencedColumnName = "id")
    )
    @Cascade(CascadeType.ALL)
    private List<ItemDeCatalogo> items;

    @Column(name = "precio")
    private Float precio;

    @Column(name = "cantidad")
    private Integer stock;


    public Oferta(String nombre, String rutaImagen, String nombreImagen,List<ItemDeCatalogo> items, Integer stock) {
        this.nombre = nombre;
        this.rutaImagen = rutaImagen;
        this.nombreImagen = nombreImagen;
        this.items = items;
        this.stock = stock;
        this.precio = this.puntosNecesarios();
    }

    public Float puntosNecesarios() {

        precio = items.stream()
                .map(ItemDeCatalogo::obtenerPrecioTotal) // Obtenemos el precio de cada item (o puntos si es otro campo)
                .reduce(0f, Float::sum);
        return precio; // Sumamos todos los precios
    }


/*
    @ManyToMany
    @JoinTable(
        name = "item_de_catalogo_por_oferta",
        joinColumns = @JoinColumn(name = "id_item_de_catalogo", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_item_de_catalogo", referencedColumnName = "id")
    )
    private List<ItemDeCatalogo> itemsDeCatalogo;

    //LO NUEVO ^^^ -------------- LO VIEJO vvv

    @ManyToMany
    @JoinTable(
        name = "producto_por_oferta",
        joinColumns = @JoinColumn(name = "id_oferta", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_producto", referencedColumnName = "id")
    )
    private List<Producto> productos;

    @ManyToMany
    @JoinTable(
        name = "servicio_por_oferta",
        joinColumns = @JoinColumn(name = "id_oferta", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_servicio", referencedColumnName = "id")
    )
    private List<Servicio> servicios;

 */
}
