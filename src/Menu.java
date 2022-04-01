import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Klasa implementująca GUI paska menu na górze programu
 */
public class Menu {

    /**
     * Konstruktor klasy <code>Menu</code> przyjmujący obiekt klasy JFrame, w którym ma być umieszczone menu
     * @param okno obiekt klasy JFrame
     */
    public Menu(JFrame okno){

        JMenuBar mb = new JMenuBar();

        /*
         * Deklaracja elementów menu
         */
        JMenu otworz = new JMenu("Otwórz");
        otworzListener(otworz);

//        JMenu oAutorach = new JMenu("o Autorach");
//        oAutorach.addMouseListener(new MouseInputAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e);
//                oAutorach.setSelected(false);
 //               new OAutorach(okno);
//            }
//        });

        JMenu zapisz = new JMenu("Zapisz");
        JMenuItem zapisProjektu = new JMenuItem("Zapis Projektu");
        zapisProjektuListener(zapisProjektu);


        JMenuItem eksportWideo = new JMenuItem("Eksport Wideo");
        eksportWideoListener(eksportWideo, okno);


        JMenu cofnij = new JMenu("Cofnij");
        cofnijListener(cofnij);


        mb.add(otworz);
        mb.add(zapisz);
//        mb.add(oAutorach);
        mb.add(cofnij);
        zapisz.add(zapisProjektu);
        zapisz.add(eksportWideo);
        okno.add(mb);
        okno.setJMenuBar(mb);
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku cofania ostatniego działąnia
     * @param cofnij cofnij
     */
    private void cofnijListener(JMenu cofnij) {
        cofnij.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Stos.cofnijOperacje();
                cofnij.setSelected(false);
            }
        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku do eksportowania wideo
     * @param eksportWideo przycisk
     * @param okno obiekt klasy JFrame
     */
    private void eksportWideoListener(JMenuItem eksportWideo, JFrame okno) {
        eksportWideo.addActionListener(e -> {
            eksportWideo.setSelected(false);
            if(GUI.lewyPanel.osCzasu.size()!=0) new OknoEksportu(okno);
            else{
                JOptionPane.showMessageDialog(null,"Oś czasu jest pusta.","Błąd",JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku zapisywania projektu
     * @param zapisProjektu zapisywanie projektu
     */
    private void zapisProjektuListener(JMenuItem zapisProjektu) {
        zapisProjektu.addActionListener(e -> {
            zapisProjektu.setSelected(false);
            try {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter(".dat", "dat"));
                fc.setAcceptAllFileFilterUsed(false);
                fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int i = fc.showOpenDialog(null);
                if(i == JFileChooser.APPROVE_OPTION){
                    File f = fc.getSelectedFile();
                    String filepath = f.getPath();
                    System.out.println(filepath);
                    Stos.zapisanieStosu(filepath);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Metoda zawierająca implementację interfejsu <code>ActionListener</code>
     * dla przycisku otwierania zapisanego projektu
     * @param otworz przycisk
     */
    private void otworzListener(JMenu otworz) {
        otworz.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                otworz.setSelected(false);
                super.mouseClicked(e);
                try {
                    JFileChooser fc = new JFileChooser();
                    fc.setFileFilter(new FileNameExtensionFilter(".dat", "dat"));
                    fc.setAcceptAllFileFilterUsed(false);
                    fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

                    int i = fc.showOpenDialog(null);
                    if(i == JFileChooser.APPROVE_OPTION){
                        File f = fc.getSelectedFile();
                        String filepath = f.getPath();
                        System.out.println(filepath);
                        Stos.wczytanieStosu(filepath);
                    }

                } catch (Exception ex) {}

            }
        });
    }
}
