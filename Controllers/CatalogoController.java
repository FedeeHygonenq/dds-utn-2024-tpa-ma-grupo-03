package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.dominioGeneral.contribuciones.beneficios.Oferta;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.Colaborador;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.productosYServicios.ItemDeCatalogo;
import ar.edu.utn.frba.dds.dominioGeneral.usuarios.colaborador.puntos.Canje;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.PuntosNoSuficientesException;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import ar.edu.utn.frba.dds.services.IItemDeCatalogoService;
import ar.edu.utn.frba.dds.services.IOfertaService;
import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import io.javalin.http.Context;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CatalogoController implements ICrudViewsHandler {

    private IItemDeCatalogoService itemDeCatalogoService;
    private IOfertaService ofertaService;

    public CatalogoController(IItemDeCatalogoService itemDeCatalogoService, IOfertaService ofertaService) {
        this.itemDeCatalogoService = itemDeCatalogoService;
        this.ofertaService = ofertaService;
    }

    public List<ItemDeCatalogo> buscarItemsDeCatalogo(){return itemDeCatalogoService.traerItemsDeCatalogo();}

    public Oferta buscarOfertaPorId(Long id)
    {
        return this.ofertaService.traerOfertaPorId(id);
    }

    public List<Oferta> buscarTodasLasOfertas()
    {
        return this.ofertaService.traerOfertas();
    }


    public ItemDeCatalogo buscarItemDeCatalogoPorId(Long id){return itemDeCatalogoService.traerItemDeCatalogoPorId(id).orElse(null);}

    private Double obtenerPuntosDeUsuario(Context context) {
        UsuarioYColaboradorController usuarioYColaboradorController = ServiceLocator.usuarioYColaboradorController();

        String idUsuario = context.sessionAttribute("idUsuario");

        Colaborador colaborador = usuarioYColaboradorController.buscarColaboradorPorNombreDeUsuario(idUsuario);

        return colaborador.puntosDisponiblesEnBilletera();
    }

    public void canjear(Context context) {
        String idOferta = context.formParam("idOferta");
        Oferta oferta = this.buscarOfertaPorId(Long.parseLong(idOferta));

        String idUsuario = context.sessionAttribute("idUsuario");

        UsuarioYColaboradorController usuarioYColaboradorController = ServiceLocator.usuarioYColaboradorController();

        try {
            usuarioYColaboradorController.realizarTransaccionDeOferta(idUsuario, oferta);

            oferta.setStock(oferta.getStock() - 1);

            ofertaService.agregar(oferta);

            context.status(200);
            Map<String, Object> model = new HashMap<>();
            model.put("successMessage", "¡La transacción fue exitosa!");
            context.render("modals/modalSuccess.hbs", model);

        } catch (PuntosNoSuficientesException e) {
            context.status(402);
            Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", e.getMessage());
            context.render("modals/modalError.hbs", model);
        }

    }

    public void canjesView(Context context) {
        String idUsuario = context.sessionAttribute("idUsuario");
        Colaborador colaborador = ServiceLocator.usuarioYColaboradorController().buscarColaboradorPorNombreDeUsuario(idUsuario);
        List<Canje> canjes = colaborador.getBilleteraDePuntos().getHistorialDePuntosGastados();
        Map<String, Object> model = new HashMap<>();
        model.put("canjes", canjes);
        context.render("general/catalogo/canjes.hbs", model);
    }


    @Override
    public void index(Context context) {

        List<Oferta> ofertas = this.ofertaService.traerOfertas().stream().filter(oferta-> oferta.getStock() > 0).collect(Collectors.toList());

        ofertas.forEach(Oferta::puntosNecesarios);

        Double puntosUsuario = this.obtenerPuntosDeUsuario(context);


        Map<String, Object> model = new HashMap<>();

        model.put("ofertas", ofertas);
        model.put("puntosUsuario", puntosUsuario);

        context.render("general/catalogo/catalogo.hbs", model);
    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

        //para admins



    }

    @Override
    public void save(Context context) {

        //para admins



    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

        //para admins

    }

    @Override
    public void delete(Context context) {

        //para admins

    }
}
