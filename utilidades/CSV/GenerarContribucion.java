package ar.edu.utn.frba.dds.utilidades.CSV;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.*;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;

import java.time.LocalDate;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
public class GenerarContribucion {
    public Contribucion generar(LocalDate fechaColaboracion, String formaDeColaboracion, Colaborador colaborador, Integer cantidad) {
        switch (formaDeColaboracion) {
            case "REDISTRIBUCION_VIANDAS":
                DistribucionDeViandas distribucionDeViandas = DistribucionDeViandas.builder()
                        .fechaDistribucion(fechaColaboracion).cantViandas(cantidad).build();
                distribucionDeViandas.setColaborador(colaborador);
            case "DINERO":
                 DonacionDeDinero donacionDeDinero = DonacionDeDinero.builder().monto((double) cantidad)
                        .fechaDeDonacion(fechaColaboracion).build();
                 donacionDeDinero.setColaborador(colaborador);
                return donacionDeDinero;

            case "DONACION_VIANDAS":
                DonacionDeViandas donacionDeViandas = DonacionDeViandas.builder().fechaDeDonacion(fechaColaboracion).build();
                donacionDeViandas.setColaborador(colaborador);
                return donacionDeViandas;

            case "ENTREGA_TARJETAS":
                RegistroDePersonaVulnerable registroDePersonaVulnerable = RegistroDePersonaVulnerable.builder()
                        .fechaRegistro(fechaColaboracion).build();
                registroDePersonaVulnerable.setColaborador(colaborador);
                return registroDePersonaVulnerable;
            default:
                throw new RuntimeException("Invalida FormaDeColaboracion");
        }
    }
}