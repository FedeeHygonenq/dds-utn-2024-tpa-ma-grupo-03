package ar.edu.utn.frba.dds.cronJobs;

import ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias.EstrategiaFallas;
import ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias.EstrategiaViandasColocadasRetiradas;
import ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias.EstrategiasViandasColab;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.GeneradorDeReportes;

public class MainReportesSemanales implements Runnable {

    @Override
    public void run(){
        GeneradorDeReportes generadorDeReportes = GeneradorDeReportes.builder().estrategia(new EstrategiaFallas()).build();
        generadorDeReportes.reportar(ServiceLocator.incidenteController().buscarIncidentes());

        generadorDeReportes.cambiarEstrategia(new EstrategiasViandasColab());
        generadorDeReportes.reportar(ServiceLocator.usuarioYColaboradorController().buscarColaboradores());

        generadorDeReportes.cambiarEstrategia(new EstrategiaViandasColocadasRetiradas());
        generadorDeReportes.reportar(ServiceLocator.heladeraController().buscarHeladeras());

    }
}
