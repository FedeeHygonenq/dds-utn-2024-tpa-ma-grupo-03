package ar.edu.utn.frba.dds.utilidades.notificador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {
    public String mensaje;
    public String asunto;
}
