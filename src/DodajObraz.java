import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;

/**
 * Klasa, która do określonego wideo dodaje obraz i w ten sposób generuje nowe wideo
 * implementuje interfejs <code>Dzialanie</code>
 */
public class DodajObraz implements Dzialanie, Serializable {

    /**
     * współrzędna X obrazu
     */
    private int pozycjaX;

    /**
     * współrzędna Y obrazu
     */
    private int pozycjaY;

    /**
     * szerokość obrazu
     */
    private int szerokoscObrazu;

    /**
     * wysokość obrazu
     */
    private int wysokoscObrazu;

    /**
     * ścieżka wideo, do którego dodajemy obraz
     */
    private String sciezkaWideo;

    /**
     * ścieżka do obrazu, który dodajemy
     */
    private String sciezkaObrazu;

    /**
     * ścieżka, do której zapisywany jest tworzony film
     */
    private String sciezkaWyjsciowa;


    /**
     * Konstruktor
     * @param pozycjaX współrzędna X obrazu
     * @param pozycjaY współrzędna Y obrazu
     * @param szerokoscObrazu szerokość obrazu
     * @param wysokoscObrazu wysokość obrazu
     * @param sciezkaWideo ścieżka wideo, do którego dodajemy obraz
     * @param sciezkaObrazu ścieżka do obrazu, który dodajemy
     */
    public DodajObraz(int pozycjaX, int pozycjaY, int szerokoscObrazu, int wysokoscObrazu, String sciezkaWideo, String sciezkaObrazu) {
        this.pozycjaX = pozycjaX;
        this.pozycjaY = pozycjaY;
        this.szerokoscObrazu = szerokoscObrazu;
        this.wysokoscObrazu = wysokoscObrazu;
        this.sciezkaWideo = sciezkaWideo;
        this.sciezkaObrazu = sciezkaObrazu;
        String nazwaWideo = FilenameUtils.getBaseName(Paths.get(sciezkaWideo).getFileName().toString());
        String nazwaObrazu = FilenameUtils.getBaseName(Paths.get(sciezkaObrazu).getFileName().toString());

        sciezkaWyjsciowa = new StringBuilder()
                .append(System.getProperty("user.dir"))
                .append("\\")
                .append(nazwaWideo)
                .append("_")
                .append(nazwaObrazu)
                .append(".mp4")
                .toString();
        sprawdzSciezkeWyjsciowa(nazwaWideo, nazwaObrazu);
    }

    /**
     * Metoda sprawdzająca, czy plik o danej nazwie już nie istnieje
     * i w razie potrzeby dodająca na koniec nazwy numerek
     * @param nazwaWideo nazwa wideo, do którego dodajemy obraz
     * @param nazwaObrazu nazwa obrazu, który dodajemy
     */
    private void sprawdzSciezkeWyjsciowa(String nazwaWideo, String nazwaObrazu) {
        File plik = new File(sciezkaWyjsciowa);
        int i = 1;
        while(plik.exists()){
            sciezkaWyjsciowa = new StringBuilder()
                    .append(System.getProperty("user.dir"))
                    .append("\\")
                    .append(nazwaWideo)
                    .append("_")
                    .append(nazwaObrazu)
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
        dodawanieObrazu();
        GUI.lewyPanel.dodajDoListy(sciezkaWyjsciowa);
        Stos.stos.push(this);
    }

    /**
     * Metoda, która generuje wideo z dodanym obrazem
     * @throws IOException
     */
    private void dodawanieObrazu() throws IOException{

        FFmpeg ffmpeg = new FFmpeg("G:\\Programy\\ffmpeg\\bin\\ffmpeg.exe");
        FFprobe ffprobe = new FFprobe("G:\\Programy\\ffmpeg\\bin\\ffprobe.exe");
        FFmpegBuilder builder = new FFmpegBuilder();

        builder.addInput(sciezkaWideo)
                .addInput(sciezkaObrazu);


        StringBuilder sb = new StringBuilder();
        sb.append("[1]scale=")
          .append(szerokoscObrazu)
          .append(":")
          .append(wysokoscObrazu)
          .append(",[0]overlay=")
          .append(pozycjaX)
          .append(":")
          .append(pozycjaY);

        builder.setComplexFilter(sb.toString())
                .addOutput(sciezkaWyjsciowa)
                .done();

        builder.setVerbosity(FFmpegBuilder.Verbosity.DEBUG);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        System.out.println("Done!");


    }

    @Override
    public String toString() {
        return "cofnięto dodawanie obrazu " + Paths.get(sciezkaObrazu).getFileName().toString() + " do wideo " + Paths.get(sciezkaWideo).getFileName().toString();
    }

}