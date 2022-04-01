import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class text extends JFrame implements ActionListener {

    /**
     * JTextField
     */
    static JTextField t;

    /**
     * JFrame
     */
    static JFrame f;

    /**
     * JButton
     */
    static JButton b;

    /**
     * JLabel, aby wyświetlić tekst
     */
    static JLabel l;

    /**
     * Domyślny konstruktor
     */
    text(){
    }


    /**
     * Metoda wykonująca się, gdy przycisk jest wciśnięty
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("submit")) {

            l.setText(t.getText());

            t.setText("  ");
        }
    }
}