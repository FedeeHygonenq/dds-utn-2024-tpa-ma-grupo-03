package ar.edu.utn.frba.dds.server.handlers;


import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.ContribucionInvalidaException;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class ContribucionHandler implements IHandler {
    @Override
    public void setHandle(Javalin app) {
        app.exception(ContribucionInvalidaException.class, (e, ctx) -> {
            handleException(e.getMessage(),ctx,400);
        });
    }

    private void handleException(String errorMessage, Context context, Integer status) {
        context.status(status);
        Map<String, Object> model = new HashMap<>();
        model.put("errorMessage", errorMessage);
        context.render("modals/modalError.hbs", model);
    }
}
