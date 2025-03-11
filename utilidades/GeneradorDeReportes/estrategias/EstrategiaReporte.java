package ar.edu.utn.frba.dds.utilidades.GeneradorDeReportes.estrategias;

import java.util.List;

public interface EstrategiaReporte<T> {

  public void generarReporte(String texto);

  public String armarReporte(List<T> lista);
}
