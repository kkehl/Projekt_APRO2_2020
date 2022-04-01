import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Klasa, która wyświetla okno do dodawania obrazu do wideo
 */
public class OknoDodawaniaObrazu {

    /**
     * * <code>oknoDodawaniaObrazu</code> obiekt klasy JFrame, będący wizualizacją okna do dodawania obrazu
     */
    private JFrame oknoDodawaniaObrazu;

    /**
     * <code>sciezkaWideo</code> ścieżka wideo, do którego dodajemy obraz
     */
    private String sciezkaWideo;

    /**
     * <code>listaObrazow</code> lista zawierająca obrazy z plików roboczych
     */
    private Object[] listaObrazow;

    /**
     * <code>jlistaObrazow</code> obiekt klasy JList, będący wizualizacją listy obrazów
     */
    private JList jlistaObrazow;

    /**
     * <code>polePozycjaX</code> pole tekstowe do wpisania współrzędnej X obrazu na filmie
     */
    private JTextField polePozycjaX;

    /**
     * <code>polePozycjaY</code> pole tekstowe do wpisania współrzędnej Y obrazu na filmie
     */
    private JTextField polePozycjaY;

    /**
     * <code>poleSzerokosc</code> pole tekstowe do wpisania szerokości obrazu na filmie
     */
    private JTextField poleSzerokosc;

    /**
     * <code>poleWysokosc</code>  pole tekstowe do wpisania wysokości obrazu na filmie
     */
    private JTextField poleWysokosc;


    /**
     * Konstruktor klasy <code>OknoDodawaniaObrazu</code>
     * @param okno
     */
    public OknoDodawaniaObrazu(JFrame okno){
        sciezkaWideo = GUI.lewyPanel.pliki[LewyPanel.FILE_PATH].get(GUI.lewyPanel.list.getSelectedIndex()).toString();
        JPanel panel = new JPanel();
        oknoDodawaniaObrazu = new JFrame("Dodawanie obrazu");

        oknoDodawaniaObrazu.add(panel);
        oknoDodawaniaObrazu.setSize(340, 360);
        Point punkt = new Point(okno.getX() + okno.getWidth() / 2 - oknoDodawaniaObrazu.getWidth() / 2, okno.getY() + okno.getHeight() / 2 - oknoDodawaniaObrazu.getHeight() / 2);
        oknoDodawaniaObrazu.setLocation(punkt);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        oknoDodawaniaObrazu.setResizable(false);
        oknoDodawaniaObrazu.setVisible(true);


        JLabel pozycjaOpis = new JLabel("Wpisz pozycję:");
        JLabel xOpis = new JLabel("x:");
        JLabel yOpis = new JLabel("y:");
        JPanel wymiaryPanel = new JPanel();
        polePozycjaX = new JTextField("0", 4);
        polePozycjaY = new JTextField("0", 4);

        JLabel rozmiarOpis = new JLabel("Wpisz rozmiar obrazu na filmie:");
        JLabel szerokoscOpis = new JLabel("szerokość:");
        JLabel wysokoscOpis = new JLabel("wysokość:");
        poleSzerokosc = new JTextField("100", 4);
        poleWysokosc = new JTextField("100", 4);


        panel.add(pozycjaOpis);
        panel.add(xOpis);
        panel.add(polePozycjaX);
        panel.add(yOpis);
        panel.add(polePozycjaY);

        panel.add(rozmiarOpis);
        wymiaryPanel.add(szerokoscOpis);
        wymiaryPanel.add(poleSzerokosc);
        wymiaryPanel.add(wysokoscOpis);
        wymiaryPanel.add(poleWysokosc);
        panel.add(wymiaryPanel);

        JLabel wybierzObrazOpi = new JLabel("Wybierz obraz do dodania:");
        panel.add(wybierzObrazOpi);

        listaObrazow = GUI.lewyPanel.pliki[LewyPanel.FILE_NAME].stream().filter(f ->
           ((String)f).substring(((String)f).lastIndexOf(".")).equals(".jpg")||
                   ((String)f).substring(((String)f).lastIndexOf(".")).equals(".jpeg")||
                   ((String)f).substring(((String)f).lastIndexOf(".")).equals(".png")||
                   ((String)f).substring(((String)f).lastIndexOf(".")).equals(".bmp")
        ).toArray();


        jlistaObrazow = new JList(listaObrazow);
        jlistaObrazow.setSelectedIndex(0);
        JScrollPane scrollowanaListaObrazow = new JScrollPane(jlistaObrazow);
        scrollowanaListaObrazow.setPreferredSize(new Dimension(100, 100));
        scrollowanaListaObrazow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollowanaListaObrazow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollowanaListaObrazow);

        //przycisk dodawania obrazu
        JButton dodajObrazPrzycisk = new JButton("Dodaj obraz");
        panel.add(dodajObrazPrzycisk);
        dodajObrazPrzyciskListener(dodajObrazPrzycisk);
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku dodawania obrazu do wideo
     * @param dodajObrazPrzycisk przycisk
     */
    private void dodajObrazPrzyciskListener(JButton dodajObrazPrzycisk) {
        dodajObrazPrzycisk.addActionListener(e -> {
            if(listaObrazow.length == 0){
                JOptionPane.showMessageDialog(null, "Dodaj obraz do listy plików roboczych","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(jlistaObrazow.isSelectionEmpty()){
                JOptionPane.showMessageDialog(null, "Wybierz z listy obraz, który chcesz dodać","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int pozycjaX = Integer.parseInt(polePozycjaX.getText());
            int pozycjaY = Integer.parseInt(polePozycjaY.getText());
            int szerokosc = Integer.parseInt(poleSzerokosc.getText());
            int wysokosc = Integer.parseInt(poleWysokosc.getText());
            int indexObrazu = GUI.lewyPanel.pliki[LewyPanel.FILE_NAME].indexOf(jlistaObrazow.getSelectedValue());
            String sciezkaObrazu = GUI.lewyPanel.pliki[LewyPanel.FILE_PATH].get(indexObrazu).toString();//=======================================================================
            DodajObraz dodajObraz = new DodajObraz(pozycjaX, pozycjaY, szerokosc, wysokosc, sciezkaWideo, sciezkaObrazu);
            try {
                dodajObraz.wykonaj();
                oknoDodawaniaObrazu.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

}
