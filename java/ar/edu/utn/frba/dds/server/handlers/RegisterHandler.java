package ar.edu.utn.frba.dds.server.handlers;

import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.CredencialesIncorrectasException;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.LocalidadInvalidaException;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.EdadInvalidaException;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.MedioDeComunicacionInvalidoException;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.UsuarioRepetidoException;
import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.contrasenias.ContraseniaNoValidaException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class RegisterHandler implements IHandler {

  @Override
  public void setHandle(Javalin app) {
    // Manejo de excepciones para Contraseña No Válida
    app.exception(ContraseniaNoValidaException.class, (e, context) -> {
      handleException(e.getMessage(), context, 422);
    });

    // Manejo de excepciones para Usuario Repetido
    app.exception(UsuarioRepetidoException.class, (e, context) -> {
      handleException(e.getMessage(), context, 409);
    });

    app.exception(EdadInvalidaException.class, (e, context) -> {
      handleException(e.getMessage(), context, 422);
    });

    app.exception(MedioDeComunicacionInvalidoException.class, (e, context) -> {
      handleException(e.getMessage(), context, 422);
    });

    app.exception(LocalidadInvalidaException.class, (e, context) -> {
      handleException(e.getMessage(), context, 422);
    });

    app.exception(CredencialesIncorrectasException.class, (e, context) -> {
      handleException(e.getMessage(), context, 401);
    });
  }

  private void handleException(String errorMessage, Context context, Integer status) {
    context.status(status);
    Map<String, Object> model = new HashMap<>();
    model.put("errorMessage", errorMessage);
    context.render("modals/modalError.hbs", model);
  }

}
