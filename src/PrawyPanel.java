import uk.co.caprica.vlcj.test.basic.PlayerVideoAdjustPanel;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Klasa odpowiadająca za tworzenie GUI prawego panelu:
 * * przyciski do obsługi
 * * obsługa dodawania tekstu
 * * obsługa kopiowania wideo
 */
public class PrawyPanel<f> extends JFrame  {

    /**
     * Jedyna istniejąca instancja klasy PrawyPanel
     */
    private static PrawyPanel inst = null;


    /**
     * Konstruktor
     * @param okno okno
     */
    private PrawyPanel(JFrame okno) {

        //JPANEL
        JPanel panelCalosc = new JPanel();
        panelCalosc.setBounds(975, 10, 200, 620);
        panelCalosc.setBackground(Color.gray);
        okno.add(panelCalosc);
        panelCalosc.setLayout(null);


        //JPANEL GÓRNY
        JPanel panelGorny = new JPanel();
        panelGorny.setBounds(0, 0, 200, 213);
        panelGorny.setBackground(Color.gray);
        panelGorny.setBorder(BorderFactory.createEmptyBorder(3, 15, 15, 15));
        panelCalosc.add(panelGorny);
        panelGorny.setLayout(new BorderLayout(0,5));




        //OBSŁUGA DODAWANIA TEKSTU
        JPanel panelTekstu = new JPanel(new BorderLayout(0,10));
        panelTekstu.setBackground(Color.GRAY);
        panelTekstu.setVisible(true);
        JLabel tekstDodanie = new JLabel("Dodanie tekstu: ");

        panelTekstu.add(tekstDodanie, "North");
        panelGorny.add(panelTekstu,"North");

        JPanel panelXYCzas = new JPanel(new BorderLayout(10,3));
        panelXYCzas.setBackground(Color.gray);

        JTextField x = new JTextField("x",5);
        JTextField y = new JTextField("y",5);

        zaznaczListener(x);
        zaznaczListener(y);
        JTextField czasTrwania = new JTextField("czas [s]",5);

        JTextField textWPoluTekstowym;
        text tekstDowyswietlenia = new text();

        textWPoluTekstowym = new JTextField("dodaj tekst",18);

        JButton dodaj = new JButton("Dodaj tekst");
        dodaj.addActionListener(tekstDowyswietlenia);

        panelXYCzas.add(x,"West");
        panelXYCzas.add(y, "Center");
        panelXYCzas.add(czasTrwania, "East");
        panelXYCzas.add(dodaj,"South");
        panelTekstu.add(panelXYCzas,"Center");



        panelXYCzas.add(textWPoluTekstowym,"North");

        zaznaczListener(textWPoluTekstowym);




        //OBSŁUGA KOPIOWANIA WIDEO

        JPanel kopiujPanel = new JPanel(new BorderLayout(10,3));

        kopiujPanel.setBackground(Color.gray);

        JLabel kopiujDodanie = new JLabel("Kopiowanie filmu: ");
        kopiujPanel.add(kopiujDodanie, "North");



        JButton kopiujWideo = new JButton("Kopiuj");
        JTextField czasOdPole = new JTextField("od",5);
        JTextField czasDoPole = new JTextField("do",5);
        zaznaczListener(czasDoPole);
        zaznaczListener(czasOdPole);
        zaznaczListener(czasTrwania);

        JPanel odDoPanel = new JPanel(new FlowLayout());

        odDoPanel.setBackground(Color.gray);

        odDoPanel.add(czasOdPole);
        odDoPanel.add(czasDoPole);
        kopiujPanel.add(odDoPanel,"Center");
        kopiujPanel.add(kopiujWideo,"South");
        panelGorny.add(kopiujPanel,"Center");


        kopiujPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        dodajListener(dodaj, x, y, czasTrwania, textWPoluTekstowym);
        kopiujWideoListener(kopiujWideo, czasOdPole, czasDoPole);


        // OBSŁUGA DODAWANIA OBRAZU
        JButton dodajObraz = new JButton("Dodaj obraz do wideo");
        dodajObraz.setBounds(10,235,180,25);
        panelCalosc.add(dodajObraz);
        dodajObrazListener(dodajObraz, okno);

        // OBSŁUGA DODAWANIA ROZMAZANIA
        JButton dodajRozmazanie = new JButton("Dodaj rozmazanie");
        JTextField stopienPole = new JTextField("stopień 1-10",5);
        stopienPole.setBounds(60,320,75,25);
        zaznaczListener(stopienPole);
        dodajRozmazanie.setBounds(10,350,180,25);
        panelCalosc.add(dodajRozmazanie);
        panelCalosc.add(stopienPole);
        dodajRozmazanie(dodajRozmazanie, stopienPole);


        // PRZYCISK DO EFEKTÓW I ICH ZATWIERDZENIA
        JButton efekty = new JButton("Efekty");
        efekty.setBounds(10,275,180,25);
        panelCalosc.add(efekty);
        efektyListener(efekty,okno);





    }

    private void dodajRozmazanie(JButton dodajRozmazanie, JTextField stopienPole) {
        dodajRozmazanie.addActionListener(e -> {
            String sciezkaWideo = GUI.lewyPanel.pliki[LewyPanel.FILE_PATH].get(GUI.lewyPanel.list.getSelectedIndex()).toString();
            int stopien = Integer.parseInt(stopienPole.getText());
            DodajRozmazanie dodajRozmazanie1 = new DodajRozmazanie(stopien, sciezkaWideo);
            try {
                dodajRozmazanie1.wykonaj();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Metoda tworząca jedną instancję klasy PrawyPanel,
     * jeżeli już istnieje to ją zwraca
     * @return inst
     */
    public static PrawyPanel instance(JFrame okno){
        if(inst == null)
            inst = new PrawyPanel(okno);
        return inst;
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku dodawania obrazu
     * @param dodajObraz przycisk dodawania obrazu
     * @param okno JFrame edytora wideo
     */
    private void dodajObrazListener(JButton dodajObraz, JFrame okno) {
        dodajObraz.addActionListener(e -> {
            if(GUI.lewyPanel.list.isSelectionEmpty()){
                JOptionPane.showMessageDialog(null, "Wybierz plik wideo na liście plików roboczych","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String sciezka = ((Path)GUI.lewyPanel.pliki[LewyPanel.FILE_PATH].get(GUI.lewyPanel.list.getSelectedIndex())).getFileName().toString();
            String rozszerzenie = sciezka.substring(sciezka.lastIndexOf("."));

            if(rozszerzenie.equals(".jpg")||rozszerzenie.equals(".jpeg")||rozszerzenie.equals(".png")||rozszerzenie.equals(".bmp"))
                JOptionPane.showMessageDialog(null, "Wybierz plik wideo na liście plików roboczych","Error", JOptionPane.ERROR_MESSAGE);
            else
                new OknoDodawaniaObrazu(okno);
        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>MouseInputAdapter</code>,
     * dzięki któremu po prześciu do danego pola tekstowego
     * zawartość tego pola jest zaznaczana
     * @param jtf pole tekstowe
     */
    private void zaznaczListener(JTextField jtf){
        jtf.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
             super.mouseClicked(e);
             jtf.selectAll();
            }
        });
    }


    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku kopiowania wideo
     * @param kopiujWideo przycisk
     * @param czasOdPole pole tekstowe z czasem początkowym
     * @param czasDoPole pole tekstowe z czasem końcowym
     */
    private void kopiujWideoListener(JButton kopiujWideo, JTextField czasOdPole, JTextField czasDoPole) {
        kopiujWideo.addActionListener(e -> {
            double czasOd;
            double czasDo;
            int konwerter = 1000;
            try {
                czasOd = Double.parseDouble(czasOdPole.getText());
                czasDo = Double.parseDouble(czasDoPole.getText());

                if((czasDo <= czasOd) || (GUI.srodkowyPanel.mediaPlayer.getLength() < (czasDo * konwerter))){
                    throw new Exception();
                }
                Kopiuj kopiuj = new Kopiuj(czasOd,czasDo);
                kopiuj.wykonaj();
            }
            catch(Exception e1) {
                System.out.println(e);
            }


        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku dodawania tekstu do wideo
     * @param dodaj przycisk
     * @param x pozycja X
     * @param y pozycja Y
     * @param czasTrwania czas trwania
     * @param textWPoluTekstowym tekst do wstawienia
     */
    private void dodajListener(JButton dodaj, JTextField x, JTextField y, JTextField czasTrwania, JTextField textWPoluTekstowym) {
        dodaj.addActionListener(e -> {
            int X = 0;
            int Y = 0;
            double czTrwania = 0;
            try {
                X = Integer.parseInt(x.getText());
                Y = Integer.parseInt(y.getText());
                czTrwania = Double.parseDouble(czasTrwania.getText());
            }catch(Exception e1) {
                System.out.println(e);
            }
            DodajTekst dodTekst = new DodajTekst(X,Y,czTrwania,textWPoluTekstowym);
            dodTekst.wykonaj();
        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku wyświetlającego panel z efektami wideo
     * @param efekty przycisk
     * @param okno JFrame
     */
    private void efektyListener(JButton efekty, JFrame okno) {
        efekty.addActionListener(e -> {
            JFrame oknoEfektów = new JFrame("okno efektów");
            oknoEfektów.setVisible(true);
            oknoEfektów.setSize(250,270);
            Point punkt = new Point(okno.getX() + okno.getWidth() / 2 - oknoEfektów.getWidth() / 2, okno.getY() + okno.getHeight() / 2 - oknoEfektów.getHeight() / 2);
            PlayerVideoAdjustPanel plVid = new PlayerVideoAdjustPanel(GUI.srodkowyPanel.mediaPlayer);
            JPanel jp = new JPanel();
            jp.setLayout(new BorderLayout());
            plVid.setBorder( BorderFactory.createEmptyBorder(0,0,-90,0) );
            jp.add(plVid,BorderLayout.NORTH);
            oknoEfektów.add(jp);
            oknoEfektów.setLocation(punkt);
        });
    }
}