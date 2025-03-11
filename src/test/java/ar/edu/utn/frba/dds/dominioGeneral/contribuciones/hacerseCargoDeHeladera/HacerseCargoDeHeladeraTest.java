package ar.edu.utn.frba.dds.dominioGeneral.contribuciones.hacerseCargoDeHeladera;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.hacerseCargoHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.services.externos.puntoRecomendado.IAdapterPuntosRecomendado;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class HacerseCargoDeHeladeraTest {
    @Test
    public void recomiendaPuntosHeladeraMedrano() throws IOException {
        Direccion direccionMedrano = Direccion.builder().longitud(-58.420065).latitud(-34.598450).build();
        Area areaMedrano = Area.builder().kmALaRedonda(1).puntoCentral(direccionMedrano).build();

        Direccion direccion1 = Direccion.builder().longitud(-58.4199699).latitud(-34.5964823).build();
        PuntoHeladera puntoRecom1 = PuntoHeladera.builder().nombre("CafeCortazar").direccion(direccion1).build();
        Direccion direccion2 = Direccion.builder().longitud(-58.4207247).latitud(-34.5977412).build();
        PuntoHeladera puntoRecom2 = PuntoHeladera.builder().nombre("McDonaldsCordobaYMedrano").direccion(direccion2).build();
        Direccion direccion3 = Direccion.builder().longitud(-58.4203379).latitud(-34.5988673).build();
        PuntoHeladera puntoRecom3 =PuntoHeladera.builder().nombre("BarVenancioAlmagro").direccion(direccion3).build();

        List<PuntoHeladera> puntosRecomendados = new ArrayList<>();
        puntosRecomendados.add(puntoRecom1);
        puntosRecomendados.add(puntoRecom2);
        puntosRecomendados.add(puntoRecom3);

        IAdapterPuntosRecomendado adapter = mock(IAdapterPuntosRecomendado.class);
        when(adapter.obtenerPuntos(areaMedrano)).thenReturn(puntosRecomendados);

        Heladera heladera = Heladera.builder().build();
        HacerseCargoDeHeladera hacerseCargoDeHeladera = HacerseCargoDeHeladera.builder()
                .heladera(heladera).adapterPuntosRecomendado(adapter).build();

        Assertions.assertEquals(puntosRecomendados, hacerseCargoDeHeladera.obtenerPuntosRecomendados(areaMedrano));
    }
}
