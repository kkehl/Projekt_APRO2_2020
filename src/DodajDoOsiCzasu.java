import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Klasa, która wykonuje działanie wstawienia danego wideo na oś czasu,
 * implementuje interfejs <code>Dzialanie</code>
 */
public class DodajDoOsiCzasu implements Dzialanie, Serializable {

    /**
     * ścieżka wideo, które dodajemy
     */
    private String sciezka;

    /**
     * Konstruktor
     * @param sciezka ścieżka wideo, które dodajemy
     */
    public DodajDoOsiCzasu(Path sciezka){
        this.sciezka = sciezka.toString();
    }

    @Override
    public void cofnij() {
        GUI.lewyPanel.osCzasu.usunZListyMultimediow(Paths.get(sciezka));
    }

    @Override
    public void wykonaj() {
        GUI.lewyPanel.osCzasu.dodajDoListyMultimediow(Paths.get(sciezka));
        Stos.stos.push(this);
    }

    @Override
    public String toString() {
        return "cofnięto dodanie do osi czasu - " + Paths.get(sciezka).getFileName();
    }
}