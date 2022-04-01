import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;


public class DodajRozmazanie implements Dzialanie, Serializable {
    private int stopien;
    private String sciezkaWideo;
    private String sciezkaWyjsciowa;


    public DodajRozmazanie(int stopien, String sciezkaWideo) {

        this.stopien = stopien;
        this.sciezkaWideo = sciezkaWideo;
        String nazwaWideo = FilenameUtils.getBaseName(Paths.get(sciezkaWideo).getFileName().toString());

        sciezkaWyjsciowa = new StringBuilder()
                .append(System.getProperty("user.dir"))
                .append("\\")
                .append(nazwaWideo)
                .append(".mp4")
                .toString();
        sprawdzSciezkeWyjsciowa(nazwaWideo);
    }

    private void sprawdzSciezkeWyjsciowa(String nazwaWideo) {
        File plik = new File(sciezkaWyjsciowa);
        int i = 1;
        while(plik.exists()){
            sciezkaWyjsciowa = new StringBuilder()
                    .append(System.getProperty("user.dir"))
                    .append("\\")
                    .append(nazwaWideo)
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
        dodawanieRozmazania(sciezkaWyjsciowa, stopien);
        GUI.lewyPanel.dodajDoListy(sciezkaWyjsciowa);
        Stos.stos.push(this);
    }

    private void dodawanieRozmazania(String sciezkaWyjsciowa, int stopien) throws IOException{

        FFmpeg ffmpeg = new FFmpeg("G:\\Programy\\ffmpeg\\bin\\ffmpeg.exe");
        FFprobe ffprobe = new FFprobe("G:\\Programy\\ffmpeg\\bin\\ffprobe.exe");
        FFmpegBuilder builder = new FFmpegBuilder();

        builder.addInput(sciezkaWideo);



        StringBuilder sb = new StringBuilder();
        sb.append("[0:v]boxblur=luma_radius=5:chroma_radius=")
                .append(stopien)
                .append(":luma_power=20[blurred]");

        builder.setComplexFilter(sb.toString())
                .addOutput(sciezkaWyjsciowa)
                .addExtraArgs("-map", "\"[blurred]\"")
                .addExtraArgs("-map", "\"0:a\"")
                //.setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        builder.setVerbosity(FFmpegBuilder.Verbosity.DEBUG);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a one-pass encode
        executor.createJob(builder).run();

        // Or run a two-pass encode (which is better quality at the cost of being slower)
        //       executor.createTwoPassJob(builder).run();

        System.out.println("Done!");


    }

    @Override
    public String toString() {
        return "cofnięto rozmazanie wideo " + Paths.get(sciezkaWideo).getFileName().toString() + " o stopniu " + stopien;
    }


}