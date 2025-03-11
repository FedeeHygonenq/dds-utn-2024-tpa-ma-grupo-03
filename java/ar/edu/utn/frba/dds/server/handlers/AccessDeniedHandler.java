package ar.edu.utn.frba.dds.server.handlers;

import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.CredencialesIncorrectasException;
import io.javalin.Javalin;

import java.nio.file.AccessDeniedException;

public class AccessDeniedHandler implements IHandler {

    @Override
    public void setHandle(Javalin app) {
        app.exception(AccessDeniedException.class, (e, context) -> {
            context.status(401);
            context.result("Acesso denegado");
        });
    }
}