import javax.swing.*;
import java.awt.*;

public class UserHome extends JPanel {

    private JLabel balanceLabel;
    private int balance = 4000; // editable balance

    public UserHome(AppFrame frame, String username) {
        setLayout(new BorderLayout());

        // ===== TOP BAR =====
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JButton createBtn = new JButton("+");
        JButton logoutBtn = new JButton("Logout");

        JPanel rightTop = new JPanel();
        rightTop.add(createBtn);
        rightTop.add(logoutBtn);

        topPanel.add(welcome, BorderLayout.WEST);
        topPanel.add(rightTop, BorderLayout.EAST);

        // ===== CENTER =====
        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JTextField groupIdField = new JTextField("Group ID", 15);
        groupIdField.setForeground(Color.GRAY);

        // transparent placeholder behavior
        groupIdField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (groupIdField.getText().equals("Group ID")) {
                    groupIdField.setText("");
                    groupIdField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (groupIdField.getText().isEmpty()) {
                    groupIdField.setText("Group ID");
                    groupIdField.setForeground(Color.GRAY);
                }
            }
        });

        JButton joinBtn = new JButton("Join");

        balanceLabel = new JLabel("$" + balance);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton editBalanceBtn = new JButton("Edit Balance");

        gbc.gridy = 0;
        center.add(groupIdField, gbc);

        gbc.gridy++;
        center.add(joinBtn, gbc);

        gbc.gridy++;
        center.add(new JLabel("Balance"), gbc);

        gbc.gridy++;
        center.add(balanceLabel, gbc);

        gbc.gridy++;
        center.add(editBalanceBtn, gbc);

        // ===== GROUP LIST =====
        JPanel groupList = new JPanel(new GridLayout(4, 1, 5, 5));
        groupList.setBorder(BorderFactory.createTitledBorder("Your Groups"));

        groupList.add(new JButton("school group 1"));
        groupList.add(new JButton("school group 2"));
        groupList.add(new JButton("school group 3"));
        groupList.add(new JButton("school group 4"));

        // ===== ADD =====
        add(topPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(groupList, BorderLayout.SOUTH);

        // ===== ACTIONS =====

        createBtn.addActionListener(e -> frame.showCreateGroup());

        logoutBtn.addActionListener(e -> frame.showStartHome());

        joinBtn.addActionListener(e -> {
            String id = groupIdField.getText().trim();
            if (id.isEmpty() || id.equals("Group ID")) {
                JOptionPane.showMessageDialog(frame, "Enter Group ID");
            } else {
                // simple simulation (freshman-friendly)
                JOptionPane.showMessageDialog(frame, "Invitation sent");
            }
        });

        editBalanceBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter new balance:");
            try {
                balance = Integer.parseInt(input);
                balanceLabel.setText("$" + balance);
            } catch (Exception ignored) {
                JOptionPane.showMessageDialog(frame, "Invalid amount");
            }
        });
    }
}