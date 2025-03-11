package ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.heladeras;

public class NoSeEncontraronHeladerasExcepction extends RuntimeException {
    public NoSeEncontraronHeladerasExcepction(String message) {
        super(message);
    }
}
