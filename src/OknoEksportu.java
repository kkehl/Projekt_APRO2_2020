import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import static java.lang.Integer.parseInt;

/**
 * Klasa, która wyświetla okno do ustawienia parametrów eksportowanego wideo
 */
public class OknoEksportu extends Container {

    /**
     * <code>oknoEksportu</code> obiekt klasy JFrame, będący wizualizacją okna do eksportowania wideo
     */
    private JFrame oknoEksportu;

    /**
     * <code>wybieranie</code> obiekt klasy JFileChooser do wybrania ścieżki wyjściowej
     */
    private JFileChooser wybieranie;

    /**
     * <code>fpsPole</code> pole tekstowe do wpisania wartości pożądanej liczby klatek na sekundę
     */
    private JTextField fpsPole;

    /**
     * Konstruktor klasy <code>OknoEksportu</code>
     * @param okno
     */
    public OknoEksportu(JFrame okno){
        JPanel panel = new JPanel();
        oknoEksportu = new JFrame("Parametry eksportu");
        oknoEksportu.add(panel);
        JButton lokalizacja = new JButton("Wybierz lokalizację");
        oknoEksportu.setSize(300, 250);
        Point punkt = new Point(okno.getX() + okno.getWidth() / 2 - oknoEksportu.getWidth() / 2, okno.getY() + okno.getHeight() / 2 - oknoEksportu.getHeight() / 2);
        oknoEksportu.setLocation(punkt);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 23));
        oknoEksportu.setResizable(false);
        oknoEksportu.setVisible(true);
        panel.add(lokalizacja);
        final String[] sciezWartosc = new String[1];
        lokalizacjaListener(lokalizacja, sciezWartosc);



        //lista wyborów
        String listaRozdz[]={"640x360","854x480","1280x720","1920x1080","2560x1440","3840x2160"};
        JComboBox rozdzielczosc=new JComboBox(listaRozdz);
        rozdzielczosc.setBounds(50, 50,100,80);


        JLabel rozdzOpis = new JLabel("Wybierz rozdzielczość:");
        rozdzOpis.setBounds(50,100, 250,160);
        panel.add(rozdzOpis);
        panel.add(rozdzielczosc);
        //pole tekstowe - fps
        fpsPole = new JTextField("30",4);
        fpsPole.setSize(10,10);
        fpsPoleListener(fpsPole);

        JLabel fpsOpis = new JLabel("Wpisz liczbę klatek na sekundę:");
        fpsOpis.setBounds(50,100, 250,160);
        panel.add(fpsOpis);
        panel.add(fpsPole);


        //przycisk eksportu
        JButton eksportuj = new JButton("Eksportuj");
        panel.add(eksportuj);
        eksportujListener(eksportuj, listaRozdz, rozdzielczosc, sciezWartosc);

    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku do eksportowania
     * @param eksportuj przycisk
     * @param listaRozdz tablica Stringów z rozdzielczościami
     * @param rozdzielczosc JComboBox z rozdzielczościami
     * @param sciezWartosc ścieżka wyjściowa wideo
     */
    private void eksportujListener(JButton eksportuj, String[] listaRozdz, JComboBox rozdzielczosc, String[] sciezWartosc) {
        eksportuj.addActionListener(e -> {
            try {
                int fpsWartosc= Integer.parseInt(fpsPole.getText());
                String rozdzWartosc[] = listaRozdz[rozdzielczosc.getSelectedIndex()].split("x");
                int szerokoscWartosc=Integer.parseInt(rozdzWartosc[0]);
                int wysokoscWartosc=Integer.parseInt(rozdzWartosc[1]);
                Renderowanie.renderuj(sciezWartosc[0],fpsWartosc,szerokoscWartosc,wysokoscWartosc);
                oknoEksportu.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>MouseInputListener</code>
     * dla pola z wartością pożądanej liczby klatek na sekundę
     * @param fpsPole
     */
    private void fpsPoleListener(JTextField fpsPole) {
        fpsPole.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                fpsPole.selectAll();
            }
        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku do wybierania ścieżki wyjściowej
     * @param lokalizacja przycisk
     * @param sciezWartosc ścieżka wyjściowa
     */
    private void lokalizacjaListener(JButton lokalizacja, String[] sciezWartosc) {
        lokalizacja.addActionListener(e -> {
            wybieranie = new JFileChooser();
            wybieranie.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
            wybieranie.setAcceptAllFileFilterUsed(false);
            wybieranie.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int i = wybieranie.showOpenDialog(null);
            if (i == JFileChooser.APPROVE_OPTION) {
                File f = wybieranie.getSelectedFile();
                String filepath = f.getPath();
                sciezWartosc[0] =filepath;
            }
        });
    }
}
