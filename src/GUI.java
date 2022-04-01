import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

/**
 * Główna klasa odpowiadająca za GUI
 */
public class GUI{

    /**
     * Jedyna istniejąca instancja klasy GUI
     */
    private static GUI inst = null;
    /**
     * Obiekt klasy <code>SrodkowyPanel</code>
     */
    public  static SrodkowyPanel srodkowyPanel;

    /**
     * Obiekt klasy <code>LewyPanel</code>
     */
    public  static LewyPanel lewyPanel;

    /**
     * Obiekt klasy <code>PrawyPanel</code>
     */
    public  static PrawyPanel prawyPanel;

    /**
     * Obiekt klasy <code>Menu</code>
     */
    public  static Menu menu;

    /**
     * Konstruktor GUI całego programu
     * @throws FileNotFoundException
     */
    private GUI() throws FileNotFoundException {
        JFrame okno = new JFrame();

        menu = new Menu(okno);
        lewyPanel = LewyPanel.instance(okno);
        srodkowyPanel = SrodkowyPanel.instance(okno);
        prawyPanel = PrawyPanel.instance(okno);

        okno.setSize(1200,700);
        okno.setLayout(null);
        okno.setVisible(true);
        okno.setResizable(false);
        okno.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                srodkowyPanel.mediaPlayer.release();
                srodkowyPanel.component.release();
                srodkowyPanel.mediaPlayerFactory.release();
                System.exit(0);
            }
        });
    }

    /**
     * Metoda tworząca jedną instancję klasy GUI,
     * jeżeli już istnieje to ją zwraca
     * @return
     * @throws FileNotFoundException
     */
    public static GUI instance() throws FileNotFoundException {
        if(inst == null)
            inst = new GUI();
        return inst;
    }

    /**
     * Metoda main uruchamiająca program
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        GUI.instance();
    }
}