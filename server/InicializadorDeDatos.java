package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.Contribucion;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.DonacionDeDinero;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.DonacionDeViandas;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.RegistroDePersonaVulnerable;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.BeneficiosPorPuntos;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.hacerseCargoHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.datos.tipos.*;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Area;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ubicacion.Direccion;

import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Heladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.ModeloHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.PuntoHeladera;
import ar.edu.utn.frba.dds.dominioGeneral.heladeras.Vianda;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Contacto;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.PersonaEnSituacionVulnerable;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Tecnico.Tecnico;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.Usuario;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.productosYServicios.ItemDeCatalogo;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.tarjeta.Tarjeta;
import ar.edu.utn.frba.dds.server.roles.TipoRol;
import ar.edu.utn.frba.dds.services.imp.ColaboradorService;
import ar.edu.utn.frba.dds.services.imp.HeladeraService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class InicializadorDeDatos {

    private static Direccion direccionMedrano = new Direccion(-58.41978440927881, -34.59849920123843);
    private static Direccion direccionCampus = new Direccion(-58.46807602392379, -34.6592578698285);

    public static void init() {
        ServiceLocator.heladeraService();
        ServiceLocator.colaboradorService();
        ServiceLocator.contribucionService();
        ServiceLocator.tarjetaController();
        ServiceLocator.sensorController();
        ServiceLocator.incidenteController();
        ServiceLocator.suscripcionController();
    }

    public static void initDev() {
        ColaboradorService colabService = ServiceLocator.colaboradorService();

        //Admin
        Usuario admin = new Usuario("admin", ServiceLocator.usuarioService().hashearContrasenia("admin"), true, true, TipoRol.ADMINISTRADOR);

        //Colab humano
        Usuario usuarioColaboradorHumano = new Usuario("pepe", ServiceLocator.usuarioService().hashearContrasenia("pepe"), true, false, TipoRol.COLABORADOR_HUMANO);
        Contacto contactoColabHumano = new Contacto(ServiceLocator.comunicacionMail(), "testsdds2024@gmail.com");
        Tarjeta tarjetaHumano = new Tarjeta("humano1234", TipoPersona.Colaborador);
        Colaborador colaboradorHumano = new Colaborador(usuarioColaboradorHumano, TipoColaborador.HUMANO, TipoDocumento.DNI, "12345678", List.of(contactoColabHumano), "Pepe", "Perez", new Direccion(-58.396123, -34.615971), null, tarjetaHumano);

        //Colab juridico
        Usuario usuarioColaboradorJuridico = new Usuario("utn", ServiceLocator.usuarioService().hashearContrasenia("utn"), true, false, TipoRol.COLABORADOR_JURIDICO);
        Contacto contactoColabJuridico = new Contacto(ServiceLocator.comunicacionMail(), "testsdds2024@gmail.com");
        Colaborador colaboradorJuridico = new Colaborador(usuarioColaboradorJuridico, TipoColaborador.JURIDICO, TipoOrganizacion.INSTITUCION, List.of(contactoColabJuridico), new Direccion(-58.376133, -34.605921), TipoRubro.EDUCACION, "Universidad tecnologica nacional", null);

        //Guardamos los colaboradores
        colabService.guardarOActualizarColaborador(colaboradorHumano);
        colabService.guardarOActualizarColaborador(colaboradorJuridico);


        ServiceLocator.usuarioService().agregar(admin);
        crearColaboracionesColabHumano(colaboradorHumano);
        crearColaboracionesColabJuridico(colaboradorJuridico);
        agregarViandasAHeladeras();
        agregarTecnicos();
        agregarSensores();

    }

    private static void crearColaboracionesColabHumano(Colaborador colaboradorHumano) {
        List<Contribucion> contribuciones = new ArrayList<>();

        //Donacion de dinero
        DonacionDeDinero donacionDeDinero = new DonacionDeDinero(1000.0, 1, colaboradorHumano);
        contribuciones.add(donacionDeDinero);

        //Donacion de viandas
        Vianda vianda = new Vianda("Ensalda rusa", LocalDate.now().plusMonths(1L), 200, 0.5);
        Vianda vianda2 = new Vianda("Sandwich JyQ", LocalDate.now().plusDays(10L), 100, 0.2);
        DonacionDeViandas donacionDeViandas = new DonacionDeViandas(vianda, colaboradorHumano);
        DonacionDeViandas donacionDeViandas2 = new DonacionDeViandas(vianda2, colaboradorHumano);
        contribuciones.add(donacionDeViandas);
        contribuciones.add(donacionDeViandas2);

        //Registro de persona vulnerable
        Tarjeta tarjeta = new Tarjeta("1234123412341234", TipoPersona.PersonaVulnerable);
        PersonaEnSituacionVulnerable personaEnSituacionVulnerable = new PersonaEnSituacionVulnerable(null, "Juan", "Perez", LocalDate.now().minusYears(20L), new Direccion(-58.376235, -34.606023), TipoDocumento.DNI, "24156235", tarjeta, new ArrayList<>());
        RegistroDePersonaVulnerable registroDePersonaVulnerable = new RegistroDePersonaVulnerable(tarjeta, personaEnSituacionVulnerable, colaboradorHumano);
        contribuciones.add(registroDePersonaVulnerable);

        //Guardar
        contribuciones.forEach(colaboradorHumano::agregarContribucion);
        ServiceLocator.colaboradorService().guardarOActualizarColaborador(colaboradorHumano);
    }

    private static void crearColaboracionesColabJuridico(Colaborador colaboradorJuridico) {
        List<Contribucion> contribuciones = new ArrayList<>();

        //Donacion de dinero
        DonacionDeDinero donacionDeDinero = new DonacionDeDinero(1000.0, 1, colaboradorJuridico);
        contribuciones.add(donacionDeDinero);

        //Hacerse cargo de heladera
        ModeloHeladera modelo1 = new ModeloHeladera("Modelo 1", 30.0f, -10.0f, 5);
        ModeloHeladera modelo2 = new ModeloHeladera("Modelo 2", 35.0f, -5.0f, 4);
        ServiceLocator.heladeraService().agregarModeloDeHeladera(modelo1);
        ServiceLocator.heladeraService().agregarModeloDeHeladera(modelo2);
        Heladera heladeraMedrano = new Heladera(new PuntoHeladera("Heladera Medrano", direccionMedrano), modelo1);
        Heladera heladeraCampus = new Heladera(new PuntoHeladera("Heladera Campus", direccionCampus), modelo2);
        HacerseCargoDeHeladera hacerseCargoDeHeladera = new HacerseCargoDeHeladera(heladeraMedrano, colaboradorJuridico);
        HacerseCargoDeHeladera hacerseCargoDeHeladera2 = new HacerseCargoDeHeladera(heladeraCampus, colaboradorJuridico);

        contribuciones.add(hacerseCargoDeHeladera);
        contribuciones.add(hacerseCargoDeHeladera2);

        //Ofrecer beneficio
        ItemDeCatalogo itemDeCatalogo = new ItemDeCatalogo("Salamin 300g", 400.0F, 1);
        Oferta oferta = new Oferta("Salamin", "src/main/resources/archivos/img/catalogo/salamin.jpeg", "salamin.jpeg", List.of(itemDeCatalogo), 3);
        BeneficiosPorPuntos beneficiosPorPuntos = new BeneficiosPorPuntos(colaboradorJuridico, List.of(oferta));
        contribuciones.add(beneficiosPorPuntos);

        //Guardar
        contribuciones.forEach(colaboradorJuridico::agregarContribucion);
        ServiceLocator.colaboradorService().guardarOActualizarColaborador(colaboradorJuridico);
    }

    private static void agregarViandasAHeladeras() {
        HeladeraService heladeraService = ServiceLocator.heladeraService();
        List<Heladera> heladeras = heladeraService.traerHeladeras();
        List<Vianda> viandas = heladeraService.traerViandas();

        for (int i = 0; i < heladeras.size(); i++) {
            heladeras.get(i).ingresarVianda(viandas.get(i));
            heladeraService.agregarHeladera(heladeras.get(i));
        }
    }


    private static void agregarTecnicos() {
        Usuario usuarioTecnico = new Usuario("tecnico", ServiceLocator.usuarioService().hashearContrasenia("tecnico"), true, false, TipoRol.TECNICO);
        Direccion direccion = new Direccion(-58.42084973199523, -34.59261983536955);
        Area areaCobertura = new Area(direccion, 10);
        Tecnico tecnico = new Tecnico("14523626", usuarioTecnico, "Javier", "Perez", TipoDocumento.DNI, "12345678", new Contacto(ServiceLocator.comunicacionMail(), "testsdds2024@gmail.com"), areaCobertura);
        ServiceLocator.tecnicoService().guardar(tecnico);

        Usuario usuarioTecnico2 = new Usuario("tecnico-campus", ServiceLocator.usuarioService().hashearContrasenia("tecnico-campus"), true, false, TipoRol.TECNICO);
        Direccion direccion2 = new Direccion(-58.46807602392579, -34.6592578698385);
        Area areaCobertura2 = new Area(direccion2, 10);
        Tecnico tecnico2 = new Tecnico("1462886", usuarioTecnico2, "Juan", "Balduzi", TipoDocumento.DNI, "132589963", new Contacto(ServiceLocator.comunicacionMail(), "testsdds2024@gmail.com"), areaCobertura2);
        ServiceLocator.tecnicoService().guardar(tecnico2);
    }


    private static void agregarSensores() {
        List<Heladera> heladeras = ServiceLocator.heladeraService().traerHeladeras();
        for (Heladera heladera : heladeras) {
            ServiceLocator.sensorService().agregarSensoresAHeladera(heladera);
        }
    }

}
