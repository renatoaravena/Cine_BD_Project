package app;
import view.MainFrame;


public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
