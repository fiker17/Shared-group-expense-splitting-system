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

        // START PAGE
        container.add(new StartHome(this), "START");

        // AUTH PAGES
        container.add(new Login(this), "LOGIN");
        container.add(new Register(this), "REGISTER");

        add(container);
        cardLayout.show(container, "START");
        setVisible(true);
    }

    public void showView(String viewName) {
        cardLayout.show(container, viewName);
    }

    // Called after successful login
    public void showUserHome(String username) {
        container.add(new UserHome(this, username), "USER_HOME");
        cardLayout.show(container, "USER_HOME");
        revalidate();
        repaint();
    }
    public void showStartHome() {
        cardLayout.show(container, "START");
    }
    public void showCreateGroup() {
        container.add(new CreateGroup(this), "CREATE_GROUP");
        cardLayout.show(container, "CREATE_GROUP");
    }
}