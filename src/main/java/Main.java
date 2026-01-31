import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
         FirebaseService.initialize();
        SwingUtilities.invokeLater(() -> new AppFrame());
    }
}