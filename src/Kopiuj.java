import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;

/**
 * Klasa wykonująca kopiowanie fragmentu wideo
 * implementuje interfejs <code>Dzialanie</code>
 */
public class Kopiuj implements Dzialanie, Serializable {

    /**
     * <code>czasOd</code> wartość w sekundach rozpoczęcia kopiowania
     */
    private double czasOd;

    /**
     * <code>czasDo</code>wartość w sekundach zakończenia kopiowania
     */
    private double czasDo;

    /**
     * <code>sciezkaWyjsciowa</code> ścieżka, do której zostanie zapisany skopiowany fragment
     */
    private String sciezkaWyjsciowa;

    /**
     * <code>sciezkaAktualnegoWideo</code> ścieżka do aktualnie otwarzanego wideo
     */
    private String sciezkaAktualnegoWideo;


    /**
     * Konstruktor
     * @param czasOd wartość w sekundach czasu początkowego
     * @param czasDo wartość w sekundach czasu końcowego
     */
    public Kopiuj(double czasOd, double czasDo){
        this.czasDo = czasDo;
        this.czasOd = czasOd;
        sciezkaAktualnegoWideo = GUI.lewyPanel.pliki[LewyPanel.FILE_PATH].get(GUI.lewyPanel.list.getSelectedIndex()).toString();
        StringBuilder sb = new StringBuilder().append(Paths.get(sciezkaAktualnegoWideo).getFileName().toString());
        sb.delete(sb.length() - 4, sb.length());
        String nazwaPlikuWejsciowego = sb.toString();
        sciezkaWyjsciowa = new StringBuilder()
                .append(System.getProperty("user.dir"))
                .append("\\")
                .append(nazwaPlikuWejsciowego)
                .append(czasOd)
                .append("-")
                .append(czasDo)
                .append(".mp4")
                .toString();
        sprawdzSciezkeWyjsciowa(nazwaPlikuWejsciowego);
    }

    /**
     * Metoda sprawdzająca, czy plik o danej nazwie już nie istnieje
     * i w razie potrzeby dodająca na koniec nazwy numerek
     * @param nazwaPlikuWejsciowego ścieżka, którą sprawdzamy
     */
    private void sprawdzSciezkeWyjsciowa(String nazwaPlikuWejsciowego) {
        File plik = new File(sciezkaWyjsciowa);
        int i = 1;
        while(plik.exists()){
            sciezkaWyjsciowa = new StringBuilder()
                    .append(System.getProperty("user.dir"))
                    .append("\\")
                    .append(nazwaPlikuWejsciowego)
                    .append(czasOd)
                    .append("-")
                    .append(czasDo)
                    .append("(")
                    .append(i)
                    .append(").mp4")
                    .toString();
            plik = new File(sciezkaWyjsciowa);
            i++;
        }
    }

    @Override
    public void cofnij() {
        GUI.lewyPanel.usunZListy(sciezkaWyjsciowa);
        File plik = new File(sciezkaWyjsciowa);
        plik.deleteOnExit();
    }


    @Override
    public void wykonaj() throws IOException {
      Renderowanie.przytnij(czasOd, czasDo, sciezkaAktualnegoWideo, sciezkaWyjsciowa);
      GUI.lewyPanel.dodajDoListy(sciezkaWyjsciowa);
      Stos.stos.push(this);
    }


    @Override
    public String toString() {
        return "cofnięto kopiowanie czas od " + czasOd + " - czas do " + czasDo;
    }

}