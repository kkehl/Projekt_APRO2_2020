import java.io.IOException;

/**
 * Interfejs jednego działania wykonywanego przez użytkownika,
 * którego obiekt jest wrzucany na stos działań
 */
public interface Dzialanie {

    /**
     * Metoda cofająca dane działanie
     */
    void cofnij();

    /**
     * Metoda wykonująca dane działanie
     * @throws IOException
     */
    void wykonaj() throws IOException;
}
