package ar.edu.utn.frba.dds.utilidades.calculadoras;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CalculadoraDistancia {

    public Direccion obtenerDireccionMasCercana(Direccion origen, Direccion direccion1, Direccion ... direcciones){

        Direccion direccionMasCercana = direccion1;
        Double distanciaMinima = distanciaEntrePuntos(origen,direccionMasCercana);;

        for(Direccion direccion : direcciones){
            Double distancia = distanciaEntrePuntos(origen, direccion);

            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                direccionMasCercana = direccion;
            }
        }

        return direccionMasCercana;
    }

    public Direccion obtenerDireccionMasCercana(Direccion origen, List<Direccion> direcciones){

        if (direcciones.isEmpty()){
            throw new IllegalArgumentException("La lista de direcciones está vacía. No se puede encontrar una dirección cercana.");
        }

        Direccion direccionMasCercana = direcciones.get(0);
        Double distanciaMinima = distanciaEntrePuntos(origen,direccionMasCercana);;

        for(Direccion direccion : direcciones){
            Double distancia = distanciaEntrePuntos(origen, direccion);

            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                direccionMasCercana = direccion;
            }
        }

        return direccionMasCercana;
    }


    public Double distanciaEntrePuntos(Direccion direccion1, Direccion direccion2) {
        double latitud1 = Math.toRadians(direccion1.getLatitud());
        double latitud2 = Math.toRadians(direccion2.getLatitud());
        double longitud1 = Math.toRadians(direccion1.getLongitud());
        double longitud2 = Math.toRadians(direccion2.getLongitud());

        double radioTierraKm = 6371; // Radio de la Tierra en kilómetros

        double deltaLatitud = latitud2 - latitud1;
        double deltaLongitud = longitud2 - longitud1;

        double a = Math.sin(deltaLatitud / 2) * Math.sin(deltaLatitud / 2) +
            Math.cos(latitud1) * Math.cos(latitud2) *
                Math.sin(deltaLongitud / 2) * Math.sin(deltaLongitud / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

      return radioTierraKm * c;
    }

}

