package ar.edu.utn.frba.dds.utilidades.ValidadorDeContrasenias;

public enum PoliticasDeContrasenia{

    LONGITUD(new PoliticaDeLongitud()),
    FUERTE(new PoliticaDeContraseniaFuerte());

    private final PoliticaDeContrasenia politica;

    PoliticasDeContrasenia(PoliticaDeContrasenia politica) {
        this.politica = politica;
    }

    public PoliticaDeContrasenia getPolitica() {
        return politica;
    }
}
