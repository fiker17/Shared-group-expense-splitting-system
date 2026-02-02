import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;

    private String currentUserEmail;

    public AppFrame() {

        setTitle("Shared Expense App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // Start Page
        container.add(new StartHome(this), "START");

        // Auth
        container.add(new Login(this), "LOGIN");
        container.add(new Register(this), "REGISTER");

        add(container);

        cardLayout.show(container, "START");
        setVisible(true);
    }


    // =============================
    // Navigation
    // =============================

    public void showView(String name) {
        cardLayout.show(container, name);
    }


    // After Login
    public void showUserHome(String email) {

        this.currentUserEmail = email;

        container.add(new UserHome(this, email), "USER_HOME");

        cardLayout.show(container, "USER_HOME");

        revalidate();
        repaint();
    }


    public String getCurrentUserEmail() {
        return currentUserEmail;
    }


    public void showStartHome() {
        cardLayout.show(container, "START");
    }


    // =============================
    // Group Pages
    // =============================

    public void showCreateGroup() {

        container.add(
                new CreateGroup(this, currentUserEmail),
                "CREATE_GROUP"
        );

        cardLayout.show(container, "CREATE_GROUP");
    }


    public void showJoinGroup() {

        container.add(
                new JoinGroup(this, currentUserEmail),
                "JOIN_GROUP"
        );

        cardLayout.show(container, "JOIN_GROUP");
    }


    // Open Group Page (NEW SYSTEM)
    public void openGroupPage(Group group, String userEmail) {

        GroupPage page =
                new GroupPage(this, group, userEmail);

        container.add(page, "GROUP_PAGE");

        cardLayout.show(container, "GROUP_PAGE");

        revalidate();
        repaint();
    }

}
