package ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.*;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BilleteraDePuntosTest {
    @Test
    public void calculaPuntosCorrectamente(){
        DonacionDeDinero donacionDeDinero = DonacionDeDinero.builder().monto(1000.00).build();
        DistribucionDeViandas distribucionDeViandas = DistribucionDeViandas.builder().cantViandas(3).build();
        RegistroDePersonaVulnerable registroDePersonaVulnerable = RegistroDePersonaVulnerable.builder().build();
        DonacionDeViandas donacionDeViandas = DonacionDeViandas.builder().build();

        ArrayList<Contribucion> listaContribuciones = new ArrayList<Contribucion>();
        listaContribuciones.add(donacionDeDinero);
        listaContribuciones.add(distribucionDeViandas);
        listaContribuciones.add(registroDePersonaVulnerable);
        listaContribuciones.add(donacionDeViandas);

        BilleteraDePuntos billetera = new BilleteraDePuntos();

        Colaborador colaborador = Colaborador.builder()
                .contribuciones(listaContribuciones).billeteraDePuntos(billetera).build();

        Assertions.assertEquals(506.5, colaborador.puntosDisponiblesEnBilletera());
    }
}
