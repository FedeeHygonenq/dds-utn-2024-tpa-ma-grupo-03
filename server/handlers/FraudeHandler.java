package ar.edu.utn.frba.dds.server.handlers;

import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.ContribucionInvalidaException;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.FraudeException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class FraudeHandler implements IHandler {
  @Override
  public void setHandle(Javalin app) {
    app.exception(FraudeException.class, (e, ctx) -> {
      handleException(e.getMessage(),ctx,406);
    });
  }

  private void handleException(String errorMessage, Context context, Integer status) {
    context.status(status);
    Map<String, Object> model = new HashMap<>();
    model.put("errorMessage", errorMessage);
    context.render("modals/modalError.hbs", model);
  }
}
