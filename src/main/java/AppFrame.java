import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel container;
    private String currentUserEmail; // Added to store session email

    public AppFrame() {
        setTitle("Shared Expense App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        container.add(new StartHome(this), "START");
        container.add(new Login(this), "LOGIN");
        container.add(new Register(this), "REGISTER");

        add(container);
        cardLayout.show(container, "START");
        setVisible(true);
    }

    public void setCurrentUserEmail(String email) { this.currentUserEmail = email; }
    public String getCurrentUserEmail() { return currentUserEmail; }

    public void showView(String viewName) { cardLayout.show(container, viewName); }

    public void showUserHome(String username) {
        container.add(new UserHome(this, username), "USER_HOME");
        cardLayout.show(container, "USER_HOME");
    }

    public void showCreateGroup() {
        container.add(new CreateGroup(this), "CREATE_GROUP");
        cardLayout.show(container, "CREATE_GROUP");
    }

    public void showStartHome() { cardLayout.show(container, "START"); }
}