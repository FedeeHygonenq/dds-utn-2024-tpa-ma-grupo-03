package ar.edu.utn.frba.dds.dominioGeneral.usuarios;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculadoraDeDistanciaTest {
        Direccion direccionMedrano = Direccion.builder().longitud(-58.41978440927881).latitud(-34.59849920123843).build();
        Direccion direccionCampus = Direccion.builder().longitud(-58.46776297183248).latitud(-34.65944510658502).build();
        Direccion tecnicoMedrano = Direccion.builder().longitud(-58.42084973199523).latitud(-34.59261983536955).build();
    @Test //Vemos si el tecnico que se ubica en campus llega a cubrir medrano con 50km a la redonda
    public void campusEnAreaCubierta(){
        Area areaCubierta = Area.builder().puntoCentral(tecnicoMedrano).kmALaRedonda(50).build();

        Tecnico tecnico = Tecnico.builder().areaCobertura(areaCubierta).build();

        Assertions.assertTrue(tecnico.cubreUbicacion(direccionCampus));
    }

    @Test //Vemos si el tecnico que se ubica en campus NO llega a cubrir medrano con 2km a la redonda
    public void campusNoCubierto(){
        Area areaCubierta = Area.builder().puntoCentral(tecnicoMedrano).kmALaRedonda(2).build();

        Tecnico tecnico = Tecnico.builder().areaCobertura(areaCubierta).build();

        Assertions.assertFalse(tecnico.cubreUbicacion(direccionCampus));
    }

    @Test
    public void puntoMasCercano(){
        Direccion parqueCentenario = Direccion.builder().longitud(-58.43516051503239).latitud(-34.60568648891233).build();

        Direccion direccionMasCercana = ServiceLocator.calculadoraDistancia().obtenerDireccionMasCercana(direccionMedrano,direccionCampus,parqueCentenario);

        Assertions.assertEquals(parqueCentenario,direccionMasCercana);

    }
}
