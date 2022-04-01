import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.test.basic.PlayerControlsPanel;
import javax.swing.*;
import java.awt.*;

/**
 * Klasa odpowiadająca za tworzenie GUI środkowego panelu:
 * * player multimediów
 * * pasek sterowania playerem
 */
public class SrodkowyPanel{

    /**
     * Jedyna istniejąca instancja klasy SrodkowyPanel
     */
    private static SrodkowyPanel inst = null;


    /**
     * Atrybuty potrzebne do playera
     */
    public EmbeddedMediaPlayer mediaPlayer;
    public EmbeddedMediaPlayerComponent component;
    public MediaPlayerFactory mediaPlayerFactory;


    private SrodkowyPanel (JFrame okno) {
        //JPANEL
        JPanel panel = new JPanel();
        panel.setBounds(230,10,724,620);
        panel.setBackground(Color.lightGray);
        okno.add(panel);
        panel.setLayout(new BorderLayout());

        //USTAWIENIE PLAYERA
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),"C:\\Program Files\\VideoLAN\\VLC");
        component = new EmbeddedMediaPlayerComponent();
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);
        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = component.getMediaPlayer();
        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
        mediaPlayer.setPlaySubItems(true);
        panel.add(canvas, BorderLayout.CENTER);
        canvas.setVisible(true);

        //PASEK STEROWANIA PLAYEREM
        final PlayerControlsPanel controlsPanel = new PlayerControlsPanel(mediaPlayer);

        panel.add(controlsPanel, BorderLayout.SOUTH);
    }

    /**
     * Metoda tworząca jedną instancję klasy LewyPanel,
     * jeżeli już istnieje to ją zwraca
     * @return
     */
    public static SrodkowyPanel instance(JFrame okno){
        if(inst == null)
            inst = new SrodkowyPanel(okno);
        return inst;
    }

    /**
     * Metoda służąca do odtworzenia pliku o danej ścieżce
     * @param filepath - ścieżka pliku do odtworzenia
     */
    public void play(String filepath){
        String[] opts = {};
        mediaPlayer.playMedia(filepath, opts);

    }
}
