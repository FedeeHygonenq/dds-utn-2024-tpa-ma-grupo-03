package ar.edu.utn.frba.dds.server.roles;

import io.javalin.security.RouteRole;

public enum TipoRol implements RouteRole {
    COLABORADOR_JURIDICO,
    COLABORADOR_HUMANO,
    TECNICO,
    ADMINISTRADOR
}
