package ar.edu.utn.frba.dds.serviciosExternos.colaboradorRecomendado;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.DonacionDeDinero;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.DonacionDeViandas;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.*;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Contacto;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos.BilleteraDePuntos;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import ar.edu.utn.frba.dds.Controllers.dtos.ColaboradorDTOOutAPI;
import ar.edu.utn.frba.dds.server.roles.TipoRol;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.lectorProperties.LectorProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ColaboradorBuilder {

    private static Double multiplicadorDinero = LectorProperties.getDoublePropertie("dineroDonado");
    private static Double multiplicadorViandas = LectorProperties.getDoublePropertie("viandasDonadas");
    public static List<Colaborador> generarColaboradoresParaTest() {

        DonacionDeDinero donacionDeDinero1 = DonacionDeDinero.builder()
                .monto(100.0)
                .fechaDeDonacion(LocalDate.now())
                .frecuencia(1)
                .build();
        DonacionDeViandas donacionDeVianda1 = DonacionDeViandas.builder()
                .vianda(new Vianda("Fideos", LocalDate.of(2024, 12, 12), 200, 1.0))
                .fechaDeDonacion(LocalDate.now())
                .build();

        DonacionDeDinero donacionDeDinero2 = DonacionDeDinero.builder()
                .monto(100.0)
                .fechaDeDonacion(LocalDate.now())
                .frecuencia(2)
                .build();

        DonacionDeViandas donacionDeVianda2 = DonacionDeViandas.builder()
                .vianda(new Vianda("Sandwich", LocalDate.of(2024, 12, 12), 300, 1.0))
                .fechaDeDonacion(LocalDate.now())
                .build();

        DonacionDeDinero donacionDeDinero3 = DonacionDeDinero.builder()
            .monto(200.0)
            .fechaDeDonacion(LocalDate.now())
            .frecuencia(3)
            .build();


        DonacionDeDinero donacionDeDinero4 = DonacionDeDinero.builder()
                .monto(200.0)
                .fechaDeDonacion(LocalDate.now())
                .frecuencia(3)
                .build();

        Colaborador colaborador1 = Colaborador.builder()
                .nombre("Juan")
                .apellido("Perez")
                .usuario(new Usuario("juanperez", "1234",  TipoRol.COLABORADOR_HUMANO))
                .tipoColaborador(TipoColaborador.HUMANO)
                .tipoDocumento(TipoDocumento.DNI)
                .numeroDeDocumento("12345678")
                .mediosDeComunicacion(List.of(new Contacto(ServiceLocator.comunicacionMail(), "juan@Example.com")))
                .direccion(new Direccion(-10.0, 10.0))
                .contribuciones(List.of(donacionDeDinero1, donacionDeVianda1))
                .billeteraDePuntos(BilleteraDePuntos.builder().puntosGastados(0.0).historialDePuntosGastados(new ArrayList<>()).build())
                .tarjetas(List.of(Tarjeta.builder().id("0987654321").titular(TipoPersona.Colaborador).fechaDeAlta(LocalDate.of(2024, 1, 1)).build()))
                .fechaDeRegistro(LocalDate.of(2024, 1, 1))
                .build();
        colaborador1.puntosDisponiblesEnBilletera();
        donacionDeDinero1.setColaborador(colaborador1);
        donacionDeVianda1.setColaborador(colaborador1);


        Colaborador colaborador2 = Colaborador.builder()
                .nombre("Juana")
                .apellido("Sanchez")
                .usuario(new Usuario("juansanchez", "4321", TipoRol.COLABORADOR_HUMANO))
                .tipoColaborador(TipoColaborador.HUMANO)
                .tipoDocumento(TipoDocumento.DNI)
                .numeroDeDocumento("87654321")
                .mediosDeComunicacion(List.of(new Contacto(ServiceLocator.comunicacionMail(), "juana@Example.com")))
                .direccion(new Direccion(10.0, -10.0))
                .contribuciones(List.of(donacionDeDinero2, donacionDeVianda2, donacionDeDinero3))
                .billeteraDePuntos(BilleteraDePuntos.builder().puntosGastados(0.0).historialDePuntosGastados(new ArrayList<>()).build())
                .tarjetas(List.of(Tarjeta.builder().id("1234567890").titular(TipoPersona.Colaborador).fechaDeAlta(LocalDate.of(2024, 2, 2)).build()))
                .fechaDeRegistro(LocalDate.of(2024, 2, 2))
                .build();
        colaborador2.puntosDisponiblesEnBilletera();
        donacionDeDinero2.setColaborador(colaborador2);
        donacionDeVianda2.setColaborador(colaborador2);
        donacionDeDinero3.setColaborador(colaborador2);

        Colaborador colaborador3 = Colaborador.builder()
                .usuario(new Usuario("empresa", "4321",TipoRol.COLABORADOR_JURIDICO))
                .tipoColaborador(TipoColaborador.JURIDICO)
                .rubro(TipoRubro.IT)
                .razonSocial("Empresa S.A.")
                .tipoOrganizacion(TipoOrganizacion.EMPRESA)
                .mediosDeComunicacion(List.of(new Contacto(ServiceLocator.comunicacionMail(), "empresa@Example.com")))
                .direccion(new Direccion(10.0, -10.0))
                .contribuciones(List.of(donacionDeDinero4))
                .billeteraDePuntos(BilleteraDePuntos.builder().puntosGastados(0.0).historialDePuntosGastados(new ArrayList<>()).build())
                .fechaDeRegistro(LocalDate.of(2024, 3, 3))
                .build();
        colaborador3.puntosDisponiblesEnBilletera();
       donacionDeDinero4.setColaborador(colaborador3);

        guardarViandas(List.of(donacionDeVianda1.getVianda(), donacionDeVianda2.getVianda()));
        return List.of(colaborador1, colaborador2, colaborador3);
    }


    public static List<ColaboradorDTOOutAPI> generarColaboradoresDTOOUTParaTest() {
        ColaboradorDTOOutAPI colaboradorDTOOutAPI1 = generarColaboradorDTOOutAPI("Juan", "Perez", "juan@Example.com", 100*multiplicadorDinero + multiplicadorViandas, 2);
        ColaboradorDTOOutAPI colaboradorDTOOutAPI2 = generarColaboradorDTOOutAPI("Juana", "Sanchez", "juana@Example.com",(100+200)*multiplicadorDinero + multiplicadorViandas,3);

        return List.of(colaboradorDTOOutAPI1, colaboradorDTOOutAPI2);
    }

    private static ColaboradorDTOOutAPI generarColaboradorDTOOutAPI(String nombre, String apellido, String contacto, Double puntos, Integer cantidadContribuciones) {
        return ColaboradorDTOOutAPI.builder().nombre(nombre).apellido(apellido).contacto(contacto).puntos(puntos).cantidadDeColaboraciones(cantidadContribuciones).build();
    }

    private static void guardarViandas(List<Vianda> viandas){
        ServiceLocator.viandasRepository().guardarViandas(viandas);
    }
}
