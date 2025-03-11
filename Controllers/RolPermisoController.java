package ar.edu.utn.frba.dds.Controllers;

import ar.edu.utn.frba.dds.models.repositories.interfaces.IPermisoRepository;
import ar.edu.utn.frba.dds.models.repositories.interfaces.IRolRepository;
import ar.edu.utn.frba.dds.server.roles.Permiso;
import ar.edu.utn.frba.dds.server.roles.Rol;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import ar.edu.utn.frba.dds.services.IPermisoService;
import ar.edu.utn.frba.dds.services.IRolService;
import io.javalin.http.Context;

import java.util.List;

public class RolPermisoController implements ICrudViewsHandler {

    private IRolService rolService;
    private IPermisoService permisoService;

    public RolPermisoController(IRolService rolService, IPermisoService permisoService)
    {
        this.rolService = rolService;
        this.permisoService = permisoService;
    }


    @Override
    public void index(Context context) {

    }

    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }
}
