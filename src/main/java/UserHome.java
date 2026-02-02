import javax.swing.*;
import java.awt.*;

public class UserHome extends JPanel {

    public UserHome(AppFrame frame, String username) {

        setLayout(null); // simple absolute layout (freshman style)
        setBackground(new Color(230, 230, 230));

        // Welcome text
        JLabel welcomeLabel = new JLabel("welcome, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        welcomeLabel.setBounds(300, 30, 300, 30);
        add(welcomeLabel);

        // Plus button (Create Group)
        JButton createGroupBtn = new JButton("+");
        createGroupBtn.setFont(new Font("Arial", Font.BOLD, 20));
        createGroupBtn.setBounds(600, 20, 50, 40);
        add(createGroupBtn);

        createGroupBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Create Group clicked");
        });

        // Logout button
        JButton logoutBtn = new JButton("logout");
        logoutBtn.setBounds(660, 20, 90, 40);
        add(logoutBtn);

        logoutBtn.addActionListener(e -> frame.showView("HOME"));

        // Group ID field
        JTextField groupIdField = new JTextField("Group ID");
        groupIdField.setBounds(150, 90, 300, 40);
        add(groupIdField);

        // Join button
        JButton joinBtn = new JButton("join");
        joinBtn.setBounds(500, 90, 120, 40);
        add(joinBtn);

        joinBtn.addActionListener(e -> {
            String groupId = groupIdField.getText();
            JOptionPane.showMessageDialog(frame, "Joining group: " + groupId);
        });

        // Balance label
        JLabel balanceLabel = new JLabel("Balance");
        balanceLabel.setOpaque(true);
        balanceLabel.setBackground(Color.BLACK);
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setBounds(100, 160, 250, 40);
        add(balanceLabel);

        JLabel amountLabel = new JLabel("$4000");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 36));
        amountLabel.setBounds(450, 150, 200, 50);
        add(amountLabel);

        // Joined groups list
        String[] groups = {
                "school group 1",
                "school group 2",
                "school group 3",
                "school group 4"
        };

        int y = 230;
        for (String group : groups) {
            JButton groupBtn = new JButton(group);
            groupBtn.setBounds(100, y, 600, 45);
            add(groupBtn);

            groupBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(frame, "Opened " + group);
            });

            y += 55;
        }
    }
}