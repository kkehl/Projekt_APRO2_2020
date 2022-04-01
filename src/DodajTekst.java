import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
 * Klasa do dodawania tekstu do wideo
 * implementuje interfejs <code>Dzialanie</code>
 */
public class DodajTekst implements Dzialanie, Serializable {

    /**
     * współrzędną X położenia tesktu
     */
    private int x;

    /**
     * współrzędną Y położenia tesktu
     */
    private int y;

    /**
     * wartość w sekundach czasu trwania wyświetlania tekstu
     */
    private double czasTrwania;

    /**
     * tekst, który dodajemy
     */
    private String st;

    /**
     * Zmienna określająca w której sekundzie zacznie się wyświetlanie tesktu
     */
    private long poczatek = 0;

    /**
     * Konwerter z sekund na milisekundy
     */
    private final int KONWERTER = 1000;


    /**
     * Konstruktor przyjmujący:
     * @param x współrzędną X położenia tesktu
     * @param y współrzędną Y położenia tesktu
     * @param czasTrwania wartość w sekundach czasu trwania wyświetlania tekstu
     * @param st tekst, który dodajemy
     */
    public DodajTekst(int x, int y,double czasTrwania,JTextField st){
        this.x = x;
        this.y = y;
        this.czasTrwania = czasTrwania;
        this.st = st.getText();
    }

    @Override
    public void cofnij() {}

    @Override
    public void wykonaj() {
        poczatek = GUI.srodkowyPanel.mediaPlayer.getTime();
        GUI.srodkowyPanel.mediaPlayer.setMarqueeText(st);
        GUI.srodkowyPanel.mediaPlayer.setMarqueeSize(60);
        GUI.srodkowyPanel.mediaPlayer.setMarqueeOpacity(70);
        GUI.srodkowyPanel.mediaPlayer.setMarqueeColour(Color.green);
        GUI.srodkowyPanel.mediaPlayer.setMarqueeTimeout((int)(czasTrwania* KONWERTER));
        GUI.srodkowyPanel.mediaPlayer.setMarqueeLocation(x, y);
        GUI.srodkowyPanel.mediaPlayer.enableMarquee(true);
        Stos.stos.push(this);
    }

    @Override
    public String toString() {
        return "cofnięto dodanie tekstu    x - " + x  + "   y - " + y + "    tekst - " + st +
                "   początek - " + poczatek + "ms "+"   czas trwania - " + czasTrwania* KONWERTER +"ms ";
    }
}