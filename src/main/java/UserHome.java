import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserHome extends JPanel {

    private JLabel balanceLabel;
    private int balance = 4000;

    private JPanel groupListPanel;

    private AppFrame frame;
    private String userEmail;

    public UserHome(AppFrame frame, String userEmail) {

        this.frame = frame;
        this.userEmail = userEmail;

        setLayout(new BorderLayout());

        // ================= TOP =================

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel welcome =
                new JLabel("Welcome, " + userEmail);

        welcome.setBorder(
                BorderFactory.createEmptyBorder(10,20,10,10)
        );

        JButton createBtn = new JButton("+");
        JButton logoutBtn = new JButton("Logout");

        JPanel rightTop = new JPanel();

        rightTop.add(createBtn);
        rightTop.add(logoutBtn);

        topPanel.add(welcome, BorderLayout.WEST);
        topPanel.add(rightTop, BorderLayout.EAST);


        // ================= CENTER =================

        JPanel center = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0;

        JTextField groupIdField =
                new JTextField("Group ID",15);

        groupIdField.setForeground(Color.GRAY);

        // Placeholder behavior
        groupIdField.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    public void focusGained(
                            java.awt.event.FocusEvent e) {

                        if (groupIdField.getText()
                                .equals("Group ID")) {

                            groupIdField.setText("");
                            groupIdField.setForeground(Color.BLACK);
                        }
                    }

                    public void focusLost(
                            java.awt.event.FocusEvent e) {

                        if (groupIdField.getText().isEmpty()) {

                            groupIdField.setText("Group ID");
                            groupIdField.setForeground(Color.GRAY);
                        }
                    }
                });

        JButton joinBtn = new JButton("Join");


        balanceLabel =
                new JLabel("$" + balance);

        balanceLabel.setFont(
                new Font("Segoe UI",
                        Font.BOLD,28)
        );

        JButton editBalanceBtn =
                new JButton("Edit Balance");


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


        // ================= GROUP LIST =================

        groupListPanel =
                new JPanel(new GridLayout(0,1,5,5));

        groupListPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Your Groups"
                )
        );

        loadGroups();


        // ================= ADD =================

        add(topPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(new JScrollPane(groupListPanel),
                BorderLayout.SOUTH);


        // ================= ACTIONS =================

        createBtn.addActionListener(e ->
                frame.showCreateGroup()
        );

        logoutBtn.addActionListener(e ->
                frame.showStartHome()
        );


        joinBtn.addActionListener(e -> {

            String id =
                    groupIdField.getText().trim();

            if (id.isEmpty() ||
                    id.equals("Group ID")) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Enter Group ID"
                );

                return;
            }

            // Firebase join request (no boolean return)
            FirebaseJoinGroup.requestJoin(id, userEmail);

            JOptionPane.showMessageDialog(
                    frame,
                    "Join request sent"
            );
        });


        editBalanceBtn.addActionListener(e -> {

            String input =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter new balance:"
                    );

            try {

                balance =
                        Integer.parseInt(input);

                balanceLabel.setText(
                        "$" + balance
                );

            } catch (Exception ignored) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid amount"
                );
            }
        });
    }


    // =============================
    // Load Groups From Firebase
    // =============================
    private void loadGroups() {

        groupListPanel.removeAll();

        List<Group> groups =
                FirebaseGroupReader
                        .getUserGroups(userEmail);

        if (groups.isEmpty()) {

            groupListPanel.add(
                    new JLabel("No groups yet")
            );
        }

        for (Group g : groups) {

            JButton btn =
                    new JButton(g.getGroupName());

            btn.addActionListener(e ->
                    frame.openGroupPage(g, userEmail)
            );

            groupListPanel.add(btn);
        }

        revalidate();
        repaint();
    }
}
