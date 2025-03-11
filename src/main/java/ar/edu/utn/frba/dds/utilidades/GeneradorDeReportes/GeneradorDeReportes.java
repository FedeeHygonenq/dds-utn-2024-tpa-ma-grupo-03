package ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes;

import ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias.EstrategiaReporte;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class GeneradorDeReportes {
  private EstrategiaReporte estrategia;

  public void cambiarEstrategia( EstrategiaReporte estrat){
    estrategia = estrat;
  }

  public <T> void reportar(List<T> lista){
    estrategia.generarReporte(estrategia.armarReporte(lista));
  }
}
