import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.json.simple.JSONObject;
import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Klasa odpowiadająca za tworzenie GUI lewego panelu:
 * * lista plików roboczych
 * * lista plików na osi czasu
 * * przyciski lewego panelu
 */
public class LewyPanel {

    /**
     * Jedyna istniejąca instancja klasy LewyPanel
     */
    private static LewyPanel inst = null;

    /**
     * * <code>pliki</code> tablica dwóch Arraylist:
     * * * ze ścieżkami do plików (obiekty klasy <code>Path</code>
     * * * z nazwami plików (obiekty klasy <code>String</code>
     */
    public ArrayList[] pliki;
    /**
     * <code>list</code> obiekt klasy Jlist, który tworzy interfejs listy plików roboczych
     */
    public JList list;
    /**
     * obiekt klasy OsCzasu
     */
    public OsCzasu osCzasu;
    /**
     * <code>FILE_PATH</code> index ArrayListy zawierającej ścieżki do plików
     */
    public static final int FILE_PATH = 0;
    /**
     * <code>FILE_NAME</code> index ArrayListy zawierającej nazwy plików
     */
    public static final int FILE_NAME = 1;


    /**
     * Konstruktor
     * @param okno
     * @throws FileNotFoundException
     */
    private LewyPanel(JFrame okno) throws FileNotFoundException {

        //JPANEL

        JPanel panel = new JPanel();
        panel.setBounds(10,10,200,620);
        panel.setBackground(Color.gray);
        okno.add(panel);
        panel.setLayout( new BorderLayout(0,0) );
        osCzasu = new OsCzasu();
        panel.setBorder(BorderFactory.createEmptyBorder(3, 15, 15, 15));


        pliki = new ArrayList[2];
        pliki[FILE_PATH] = new ArrayList<Path>();
        pliki[FILE_NAME] = new ArrayList<String>();



        list = new JList(pliki[FILE_NAME].toArray());
        listListener(list);


        //PANEL PLIKÓW ROBOCZYCH
        JPanel listaPlikowRoboczych = new JPanel();
        listaPlikowRoboczych.setLayout(new BorderLayout());
        listaPlikowRoboczych.setBackground(Color.gray);
        panel.add(listaPlikowRoboczych, "North");

        JLabel plikiRoboczeTekst = new JLabel("Pliki robocze:");
        listaPlikowRoboczych.add(plikiRoboczeTekst, "North");

        JScrollPane plikiRobocze = new JScrollPane(list);;
        plikiRobocze.setPreferredSize(new Dimension(150, 235));
        plikiRobocze.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        plikiRobocze.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        listaPlikowRoboczych.add(plikiRobocze, "Center");

        //PRZYCISK WCZYTANIA PLIKU
        JPanel wczytajDodaj = new JPanel(new BorderLayout());
        JButton wczytaj = new JButton("Wczytaj plik");
        wczytajDodaj.add(wczytaj, "North");
        wczytajListener(wczytaj);

        //PRZYCISK DODANIA DO OSI CZASU
        JButton dodajDoOsi = new JButton("Dodaj do osi czasu");
        wczytajDodaj.add(dodajDoOsi,"Center");
        dodajDoOsiCzasuListener(dodajDoOsi);

        listaPlikowRoboczych.add(wczytajDodaj,"South");

        //PANEL OSI CZASU
        JPanel osCzasuPanel = new JPanel(new BorderLayout());
        osCzasuPanel.setBackground(Color.gray);
        osCzasu.stworzGUI(osCzasuPanel);
        panel.add(osCzasuPanel,"Center");

        JLabel osCzasuTekst = new JLabel("Oś czasu:");
        osCzasuPanel.add(osCzasuTekst, "North");



        //PANEL OBSŁUGI OSI CZASU
        JPanel obslugaPrzyciskow = new JPanel(new GridLayout(1,3));

        //PANEL OBSŁUGI OSI CZASU
        JButton strzalkaGora = new JButton("/|\\");
        JButton strzalkaDol = new JButton("\\|/");
        JButton usun = new JButton("x");
        obslugaPrzyciskow.add(strzalkaDol);
        obslugaPrzyciskow.add(strzalkaGora);
        obslugaPrzyciskow.add(usun);
        osCzasuPanel.add(obslugaPrzyciskow,"South");



        //KONFIGURACJA W PLIKU JSON
        JsonReader jr = new JsonReader(new FileReader("konfiguracja.json"));
        Gson g = new Gson();
        JSONObject[] data = g.fromJson(jr, JSONObject[].class);

        for(int i=0;i<data.length;i++)
            dodajDoListy(data[i].get("sciezka").toString());


        //DODANIE LISTENERÓW DO PRZYCISKÓW PRZENOSZENIA PLIKÓW NA LIŚCIE OSI CZASU
        strzalkaGora.addActionListener(e -> osCzasu.przeniesWGore());
        strzalkaDol.addActionListener(e -> osCzasu.przeniesWDol());
        usun.addActionListener(e -> osCzasu.usunZListyMultimediow());

    }

    /**
     * Metoda tworząca jedną instancję klasy LewyPanel,
     * jeżeli już istnieje to ją zwraca
     * @return
     * @throws FileNotFoundException
     */
    public static LewyPanel instance(JFrame okno) throws FileNotFoundException {
        if(inst == null)
            inst = new LewyPanel(okno);
        return inst;
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku dodawania do osi czasu
     * @param dodajDoOsiCzasu
     */
    private void dodajDoOsiCzasuListener(JButton dodajDoOsiCzasu) {
        dodajDoOsiCzasu.addActionListener(e -> {
            if(list.isSelectionEmpty()) return;
            String sciezka = ((Path)pliki[FILE_PATH].get(list.getSelectedIndex())).getFileName().toString();
            String rozszerzenie = sciezka.substring(sciezka.lastIndexOf("."));
            if(rozszerzenie.equals(".jpg")||rozszerzenie.equals(".jpeg")||rozszerzenie.equals(".png")||rozszerzenie.equals(".bmp")){
                JOptionPane.showMessageDialog(null,"Nie można dodać obrazu do osi czasu.","Błąd",JOptionPane.ERROR_MESSAGE);
                return;
            }
            DodajDoOsiCzasu dodajDoOsi = new DodajDoOsiCzasu((Path)pliki[FILE_PATH].get(list.getSelectedIndex()));
            dodajDoOsi.wykonaj();
        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku wczytywania plików do listy plików roboczych
     * @param wczytaj
     */
    private void wczytajListener(JButton wczytaj) {
        wczytaj.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
            fc.addChoosableFileFilter(new FileNameExtensionFilter( "Image Files","jpg", "jpeg", "png", "bmp"));
        //    fc.setFileFilter(new FileNameExtensionFilter("Multimedia (filmy, obrazy)","avi", "mp4", "wmv", "flv", "mkv","mpeg", "mpeg1","mpeg2","mpeg3", "mpg", "jpg", "jpeg", "png", "bmp"));
            fc.setAcceptAllFileFilterUsed(false);
            fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int i = fc.showOpenDialog(null);
            if(i == JFileChooser.APPROVE_OPTION){
                File f = fc.getSelectedFile();
                String filepath = f.getPath();
                System.out.println(filepath);
                dodajDoListy(filepath);
            }
        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>MouseListener</code>
     * dla listy plików roboczych, dzięki czemu po wybraniu elementu jest on odtwarzany
     * @param list
     */
    private void listListener(JList<String> list) {
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GUI.srodkowyPanel.play(pliki[FILE_PATH].get(list.getSelectedIndex()).toString());
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

    }

    /**
     * Metoda, która dodaje element do tablicy <code>pliki</code>,
     * a następnie odświeża UI JListy
     * @param filepath
     */
    public void dodajDoListy(String filepath) {
        int wybranyIndeks = list.getSelectedIndex();
        pliki[FILE_PATH].add(Paths.get(filepath));
        pliki[FILE_NAME].add(Paths.get(filepath).getFileName().toString());
        list.setListData(pliki[FILE_NAME].toArray());
        list.updateUI();
        list.setSelectedIndex(wybranyIndeks);
    }

    /**
     * Metoda, która usuwa element z tablicy <code>pliki</code>,
     * a następnie odświeża UI JListy
     * @param filepath
     */
    public void usunZListy(String filepath) {
        pliki[FILE_PATH].remove(Paths.get(filepath));
        pliki[FILE_NAME].remove(Paths.get(filepath).getFileName().toString());
        list.setListData(pliki[FILE_NAME].toArray());
        list.updateUI();
        list.setSelectedIndex(0);
    }

}