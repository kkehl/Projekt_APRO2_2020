import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Klasa implementująca oś czasu tworzonego filmu
 */
public class OsCzasu {

    /**
     * * <code>listaMultimediów</code> lista ścieżek do plików znajdujących się na osi czasu
     */
    public ArrayList<Path> listaMultimediów;

    /**
     * <code>osCzasuGUI</code> obiekt klasy Jlist, który jest interpretacją graficzną osi czasu
     */
    private JList<Object> osCzasuGUI;

    /**
     * Konstruktor, tworzący nową listę multimediów
     */
    public OsCzasu(){
        listaMultimediów = new ArrayList<>();
    }

    /**
     * Metoda zwracająca liczbę elementów na osi czasu
     * @return liczba elementów na osi czasu
     */
    public int size(){
        return listaMultimediów.size();
    }


    @Override
    public String toString(){
        if(listaMultimediów.size() == 0)
            return "Brak elementów na osi czasu";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < listaMultimediów.size(); i++)
            sb.append(listaMultimediów.get(i).getFileName() + " ---- ");
        sb.delete(sb.length() - 5, sb.length() - 1);
        return sb.toString();
    }

    /**
     * Metoda tworząca GUI osi czasu
     * @param listy
     */
    public void stworzGUI(JPanel listy) {
        osCzasuGUI = new JList<>(listaMultimediów.stream().map(f -> f.getFileName().toString()).toArray());
        osCzasuGUI.setBounds(10,160, 180,150);
        JScrollPane plikiNaOsiCzasu = new JScrollPane(osCzasuGUI);
        plikiNaOsiCzasu.setPreferredSize(new Dimension(150, 250));
        plikiNaOsiCzasu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        plikiNaOsiCzasu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listy.add(plikiNaOsiCzasu, "Center");
    }

    /**
     * Metoda dodająca do osi czasu element o danej ścieżce i odświeżająca GUI
     * @param sciezka
     */
    public void dodajDoListyMultimediow(Path sciezka) {
        listaMultimediów.add(sciezka);
        osCzasuGUI.setListData(listaMultimediów.stream().map(f -> f.getFileName().toString()).toArray());
        osCzasuGUI.updateUI();
    }

    /**
     * Metoda usuwająca z osi czasu element o danej ścieżce i odświeżająca GUI
     * @param sciezka
     */
    public void usunZListyMultimediow(Path sciezka){
        listaMultimediów.remove(sciezka);
        osCzasuGUI.setListData(listaMultimediów.stream().map(f -> f.getFileName().toString()).toArray());
        osCzasuGUI.updateUI();
    }

    /**
     * Metoda usuwająca z osi czasu element aktualnie zaznaczony przez użytkownika
     */
    public void usunZListyMultimediow(){
        if(osCzasuGUI.isSelectionEmpty())return;
        int indexTemp = osCzasuGUI.getSelectedIndex()-1;
        listaMultimediów.remove(osCzasuGUI.getSelectedIndex());
        osCzasuGUI.setListData(listaMultimediów.stream().map(f -> f.getFileName().toString()).toArray());
        osCzasuGUI.updateUI();
        osCzasuGUI.setSelectedIndex(indexTemp);
    }

    /**
     * Metoda zamieniająca zaznaczony przez użytkownika element na osi czasu z elementem o indeksie o 1 mniejszym
     */
    public void przeniesWGore(){
        if(osCzasuGUI.getSelectedIndex() == 0 || osCzasuGUI.isSelectionEmpty()) return;
        int indexTemp = osCzasuGUI.getSelectedIndex()-1;
        Collections.swap(listaMultimediów, osCzasuGUI.getSelectedIndex(), osCzasuGUI.getSelectedIndex()-1);
        osCzasuGUI.setListData(listaMultimediów.stream().map(f -> f.getFileName().toString()).toArray());
        osCzasuGUI.updateUI();
        osCzasuGUI.setSelectedIndex(indexTemp);
    }

    /**
     * Metoda zamieniająca zaznaczony przez użytkownika element na osi czasu z elementem o indeksie o 1 większym
     */
    public void przeniesWDol(){
        if(osCzasuGUI.getSelectedIndex() == listaMultimediów.size() - 1 || osCzasuGUI.isSelectionEmpty()) return;
        int indexTemp = osCzasuGUI.getSelectedIndex()+1;
        Collections.swap(listaMultimediów, osCzasuGUI.getSelectedIndex(), osCzasuGUI.getSelectedIndex()+1);
        osCzasuGUI.setListData(listaMultimediów.stream().map(f -> f.getFileName().toString()).toArray());
        osCzasuGUI.updateUI();
        osCzasuGUI.setSelectedIndex(indexTemp);
    }

}
