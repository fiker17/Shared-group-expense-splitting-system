import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel container;
    private String currentUserEmail;

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

    /**
     * FIX 1: Updated to ensure UserHome is properly added and shown.
     * Use this when clicking "Back" from a Group Dashboard.
     */
    public void showUserHome(String emailOrUsername) {
        container.add(new UserHome(this, emailOrUsername), "USER_HOME");
        cardLayout.show(container, "USER_HOME");
        refresh();
    }

    public void showCreateGroup() {
        container.add(new CreateGroup(this), "CREATE_GROUP");
        cardLayout.show(container, "CREATE_GROUP");
    }

    public void showStartHome() { cardLayout.show(container, "START"); }

    /**
     * FIX 2: Added showAdminRole method
     * (Ensures AdminRole.java can find its target)
     */
    public void showAdminRole(String groupId) {
        container.add(new AdminRole(this, groupId), "ADMIN_ROLE");
        cardLayout.show(container, "ADMIN_ROLE");
        refresh();
    }

    /**
     * FIX 3: Corrected showGroupDashboard spelling
     * (Matches the call from UserHome and AdminRole)
     */
    public void showGroupDashboard(String groupId) {
        container.add(new GroupDashboard(this, groupId), "GROUP_DASHBOARD");
        cardLayout.show(container, "GROUP_DASHBOARD");
        refresh();
    }

    /**
     * FIX 4: Helper to handle revalidate/repaint in one spot
     */
    private void refresh() {
        container.revalidate();
        container.repaint();
    }
}