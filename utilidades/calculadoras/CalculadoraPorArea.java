package ar.edu.utn.frba.dds.utilidades.calculadoras;

import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;

import java.util.List;

public class CalculadoraPorArea {

    public List<Heladera> heladerasEnArea(List<Heladera> heladeras, Area area){
        return heladeras.stream().filter(heladera -> heladeraEstaEnArea(heladera,area)).toList();
    }

    public List<Tecnico> tecnicosEnArea(List<Tecnico> tecnicos, Area area){
        return tecnicos.stream().filter(tecnico -> tecnicoEstaEnArea(tecnico,area)).toList();
    }

    public List<Tecnico> tecnicosQueCubrenHeladera(List<Tecnico> tecnicos, Heladera heladera){
        return tecnicos.stream().filter(tecnico -> tecnico.cubreUbicacion(heladera.getPuntoEstrategico().getDireccion())).toList();
    }

    public Boolean heladeraEstaEnArea(Heladera heladera, Area area){
        return area.getKmALaRedonda() >= ServiceLocator.calculadoraDistancia()
                .distanciaEntrePuntos(heladera.getPuntoEstrategico().getDireccion(), area.getPuntoCentral());
    }

    public Boolean tecnicoEstaEnArea(Tecnico tecnico, Area area){
        return area.getKmALaRedonda() >= ServiceLocator.calculadoraDistancia()
                .distanciaEntrePuntos(tecnico.getAreaCobertura().getPuntoCentral(), area.getPuntoCentral());
    }
}
