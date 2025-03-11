package ar.edu.utn.frba.dds.middlewares;

import ar.edu.utn.frba.dds.excepcionesPersonalizadas.server.AccessDeniedException;
import ar.edu.utn.frba.dds.server.roles.TipoRol;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class AuthMiddleware {

    public static void apply(Javalin app) {
        app.beforeMatched(ctx -> {
            var userRole = getUserRoleType(ctx);
            var isAuthenticated = isAuthenticated(ctx);
            if (!ctx.routeRoles().isEmpty() && !ctx.routeRoles().contains(userRole)) {
                handleAccessDenied(ctx);
            }
        });
    }

    private static TipoRol getUserRoleType(Context context) {
        return context.sessionAttribute("tipoRol") != null?
                TipoRol.valueOf(context.sessionAttribute("tipoRol")) : null;
    }

    private static Boolean isAuthenticated(Context context) {
        return context.sessionAttribute("authenticated");
    }

    private static void handleAccessDenied(Context ctx) {
        ctx.status(401);
         ctx.redirect("/acceso/denegado?redirect=" + ctx.path());
    }



}