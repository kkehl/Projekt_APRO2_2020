import javax.swing.*;
import java.io.*;
import java.util.Stack;


/**
 * Klasa zawierająca stos działań i implementująca metody wykonujące operacje na tym stosie
 */
public class Stos implements Serializable {

    /**
     * Implementacja stosu działań
     */
    public static Stack<Dzialanie> stos = new Stack<>();


    /**
     * Metoda serializująca stan stosu
     * @param sciezkaWyjsciowa sciezka pliku wyjściowego
     * @throws IOException
     */
    public static void zapisanieStosu(String sciezkaWyjsciowa)throws IOException{
        // Obsługa błednej ścieżki
        if(!sciezkaWyjsciowa.substring(sciezkaWyjsciowa.length()-4).equals(".dat")) {
            System.out.println("Nieprawidłowy format pliku");
            return;
        }

        // Zapisywanie projektu

        Stack<Dzialanie> stosPomocniczy = new Stack<>();
        ObjectOutputStream zapis = new ObjectOutputStream(new FileOutputStream(sciezkaWyjsciowa));
        while (!Stos.stos.isEmpty()) {
            Dzialanie dzialanie = Stos.stos.pop();
            stosPomocniczy.push(dzialanie);
            zapis.writeObject(dzialanie);

        }
        while (!stosPomocniczy.isEmpty()){
            Stos.stos.push(stosPomocniczy.pop());
        }
        zapis.close();
        System.out.println("zapisano");
    }


    /**
     * Metoda wczytująca serializowany stos
     * @param sciezkaWejsciowa sciezka pliku zawierająca serializowany stos
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void wczytanieStosu(String sciezkaWejsciowa)throws IOException,ClassNotFoundException {
        // Obsługa błędnej ścieżki

        if(!new File(sciezkaWejsciowa).exists()) {
            JOptionPane.showMessageDialog(null, "Plik nie istnieje","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!sciezkaWejsciowa.substring(sciezkaWejsciowa.length()-4).equals(".dat")) {
            System.out.println("Nieprawidłowy format pliku");
            return;
        }
        if(!Stos.stos.isEmpty()){
            int potwierdzenie =JOptionPane.showConfirmDialog(null,"Wszystkie niezapisane zmiany w aktualnym projekcie zostaną utracone.\n\nCzy na pewno chcesz wczytać projekt?\n\n","Uwaga",JOptionPane.YES_NO_OPTION);
            if(potwierdzenie == JOptionPane.NO_OPTION) return;
            else{
                while(!Stos.stos.isEmpty()) Stos.cofnijOperacje();
            }
        }

        // Wczytywanie projektu

        Stack<Dzialanie> stosPomocniczy = new Stack<>();
        ObjectInputStream odczyt = new ObjectInputStream(new FileInputStream(sciezkaWejsciowa));
        Dzialanie dzialanko;
        try {
            while ((dzialanko = (Dzialanie) odczyt.readObject()) != null) {
                stosPomocniczy.push(dzialanko);
            }
            odczyt.close();
        } catch (Exception e) {
        }finally {
            odczyt.close();
            wykonanie(stosPomocniczy);
        }
        System.out.println("wczytano");
    }

    /**
     * Metoda wykonująca po kolei działania ze stosu pomocniczego
     * @param stosPomocniczy
     * @throws IOException
     */
    public static void wykonanie(Stack<Dzialanie> stosPomocniczy) throws IOException {
        while (!stosPomocniczy.isEmpty()){
        Dzialanie dzialanie = stosPomocniczy.pop();
        dzialanie.wykonaj();
        }
    }


    /**
     * Metoda cofająca ostatnie działąnie na stosie
     */
    public static void cofnijOperacje(){

        if(!stos.isEmpty()) {
            Dzialanie dzialanie = stos.pop();
            dzialanie.cofnij();
            System.out.println(dzialanie);
        }
        else System.out.println("\nStos jest pusty! ");
    }
}



