package ar.edu.utn.frba.dds.utilidades.CSV;

import lombok.Builder;

@Builder
public class ResultadoValidacion {
    private final boolean success;
    private final String message;

    public ResultadoValidacion(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
