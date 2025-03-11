package ar.edu.utn.frba.dds.models.repositories.interfaces;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroMovimiento;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.sensores.RegistroTemperatura;

import java.util.List;

public interface IRegistrosSensor {
    void agregarRegistroMovimiento(RegistroMovimiento registroMovimiento);
    void agregarRegistroTemperatura(RegistroTemperatura registroTemperatura);
    List<RegistroMovimiento> buscarRegistrosMovimiento();
    List<RegistroTemperatura> buscarRegistrosTemperatura();
}
