package ar.edu.utn.frba.dds.dominioGeneral.usuarios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PersonaEnSituacionVulnerableTest {

    @Test
    public void quitaMayoresDeLaLista(){
        LocalDate fechaNacimiento1 = LocalDate.of(2012,3,1);
        LocalDate fechaNacimiento2 = LocalDate.of(1999,6,25);
        LocalDate fechaNacimiento3 = LocalDate.of(2004,6,17);

        PersonaEnSituacionVulnerable hijo1 = PersonaEnSituacionVulnerable.builder().fechaDeNacimiento(fechaNacimiento1).build();
        PersonaEnSituacionVulnerable hijo2 = PersonaEnSituacionVulnerable.builder().fechaDeNacimiento(fechaNacimiento2).build();
        PersonaEnSituacionVulnerable hijo3 = PersonaEnSituacionVulnerable.builder().fechaDeNacimiento(fechaNacimiento3).build();

        List<PersonaEnSituacionVulnerable> hijos = new ArrayList<PersonaEnSituacionVulnerable>();
        hijos.add(hijo1);
        hijos.add(hijo2);
        hijos.add(hijo3);

        PersonaEnSituacionVulnerable adulto = PersonaEnSituacionVulnerable.builder().menoresACargo(hijos).build();

        adulto.quitarMayoresDeLaLista();

        Assertions.assertEquals(1, (int) adulto.cantidadMenoresACargo());
    }
}
