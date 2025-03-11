import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class Main implements WithSimplePersistenceUnit {

  public static void main(String[] args) {
    Main instance = new Main();
    instance.impactarEnBase();
  }

  public void impactarEnBase() {
    withTransaction(() -> {

    });
  }

}
