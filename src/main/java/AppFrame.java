import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel container;

    public AppFrame() {
        setTitle("Shared Expense App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // Add the panels
        container.add(new Home(this), "HOME");
        container.add(new Login(this), "LOGIN");
        container.add(new Register(this), "REGISTER");

        add(container);
        setVisible(true);
    }

    public void showView(String viewName) {
        cardLayout.show(container, viewName);
    }
}
