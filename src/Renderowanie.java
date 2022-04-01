import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Klasa służąca do renderowania końcowego wideo i do przycinania wideo
 */
public class Renderowanie {

    /**
     * Metoda służąca do renderowania wideo
     * @param sciezkaWyjsciowa sciezka pliku generowanego pliku
     * @param fps wartość klatek na sekundę renderowanego wideo
     * @param szerokosc szerokość renderowanego wideo w pikselach
     * @param wysokosc wysokość renderowanego wideo w pikselach
     * @throws IOException IOexception
     */
    public static void renderuj(String sciezkaWyjsciowa, int fps, int szerokosc, int wysokosc) throws IOException {

        if (GUI.lewyPanel.osCzasu.size()==0) {
            JOptionPane.showMessageDialog(null, "Oś czasu jest pusta :(","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
            FFmpeg ffmpeg = new FFmpeg("G:\\Programy\\ffmpeg\\bin\\ffmpeg.exe");
            FFprobe ffprobe = new FFprobe("G:\\Programy\\ffmpeg\\bin\\ffprobe.exe");
            FFmpegBuilder builder = new FFmpegBuilder();

            for (int i = 0; i < GUI.lewyPanel.osCzasu.size(); i++)
                builder.addInput(GUI.lewyPanel.osCzasu.listaMultimediów.get(i).toString());

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < GUI.lewyPanel.osCzasu.listaMultimediów.size(); i++)
                sb.append("[").append(i).append(":v]scale=").append(szerokosc).append(":").append(wysokosc).append(":force_original_aspect_ratio=increase:[v").append(i).append("];");

            for (int i = 0; i < GUI.lewyPanel.osCzasu.listaMultimediów.size(); i++)
                sb.append("[v").append(i).append("][").append(i).append(":a:0]");



            sb.append("concat=n=").append(GUI.lewyPanel.osCzasu.listaMultimediów.size()).append(":v=1:a=1[outv][outa]");

            builder.setComplexFilter(sb.toString())

                    .addOutput(sciezkaWyjsciowa)
                    .addExtraArgs("-map", "\"[outv]\"")
                    .addExtraArgs("-map", "\"[outa]\"")
                    .setVideoFrameRate(fps, 1)
                    .setAudioCodec("libmp3lame")
                    .setVideoCodec("libx264")
                    .addExtraArgs("-vsync", "2")
                    //.setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .done();

            builder.setVerbosity(FFmpegBuilder.Verbosity.DEBUG);
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder).run();

            System.out.println("Done!");
    }

    /**
     * Metoda przycinająca wideo o określonej ścieżce i zapisująca je w nowym pliku
     * @param czasOd czas początkowy
     * @param czasDo czas końcowy
     * @param sciezkaWejsciowa ścieżka pliku, z któego kopiujemy fragment
     * @param sciezkaWyjsciowa ścieżka pliku, do któego zapisujemy skopiowany fragment
     * @throws IOException IOexception
     */
    public static void przytnij(double czasOd, double czasDo, String sciezkaWejsciowa, String sciezkaWyjsciowa) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("G:\\Programy\\ffmpeg\\bin\\ffmpeg.exe");
        FFprobe ffprobe = new FFprobe("G:\\Programy\\ffmpeg\\bin\\ffprobe.exe");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(sciezkaWejsciowa)
                .setStartOffset((long)(czasOd*1000), TimeUnit.MILLISECONDS)
                .addOutput(sciezkaWyjsciowa)
                .setDuration((long)((czasDo*1000)-(czasOd*1000)),TimeUnit.MILLISECONDS)
                .done();

        builder.setVerbosity(FFmpegBuilder.Verbosity.DEBUG);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        System.out.println("Done!");
    }

}
