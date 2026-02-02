import javax.swing.*;
import java.awt.*;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.DocumentSnapshot;

public class UserHome extends JPanel {
    private AppFrame frame;
    private JLabel balanceLabel;
    private double balance = 0.0; // Changed to double for precise currency
    private JPanel groupListPanel;

    public UserHome(AppFrame frame, String username) {
        this.frame = frame;
        setLayout(new BorderLayout());

        // TOP BAR
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        JButton createBtn = new JButton("+");
        JButton logoutBtn = new JButton("Logout");
        JPanel rightTop = new JPanel();
        rightTop.add(createBtn); rightTop.add(logoutBtn);
        topPanel.add(welcome, BorderLayout.WEST);
        topPanel.add(rightTop, BorderLayout.EAST);

        // CENTER
        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField groupIdField = new JTextField("Group ID", 15);
        groupIdField.setForeground(Color.GRAY);

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
        balanceLabel = new JLabel("$0.00"); // Default until loaded
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JButton editBalanceBtn = new JButton("Edit Balance");

        gbc.gridy = 0; center.add(groupIdField, gbc);
        gbc.gridy++; center.add(joinBtn, gbc);
        gbc.gridy++; center.add(new JLabel("Balance"), gbc);
        gbc.gridy++; center.add(balanceLabel, gbc);
        gbc.gridy++; center.add(editBalanceBtn, gbc);

        // GROUP LIST
        groupListPanel = new JPanel();
        JScrollPane scroll = new JScrollPane(groupListPanel);
        scroll.setBorder(BorderFactory.createTitledBorder("Joined Groups"));
        scroll.setPreferredSize(new Dimension(200, 200));

        add(topPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        // --- ACTIONS ---
        createBtn.addActionListener(e -> frame.showCreateGroup());
        logoutBtn.addActionListener(e -> frame.showStartHome());

        joinBtn.addActionListener(e -> {
            String id = groupIdField.getText().trim();
            if (id.isEmpty() || id.equals("Group ID")) {
                JOptionPane.showMessageDialog(frame, "Enter Group ID");
            } else {
                boolean success = FirebaseJoinGroup.requestJoin(id, frame.getCurrentUserEmail());
                if (success) JOptionPane.showMessageDialog(frame, "Invitation sent!");
                else JOptionPane.showMessageDialog(frame, "Group not found!");
            }
        });

        editBalanceBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter new balance:");
            if (input != null) {
                try {
                    double newAmt = Double.parseDouble(input);
                    // Update Firestore directly
                    FirestoreClient.getFirestore().collection("users")
                            .document(frame.getCurrentUserEmail()).update("balance", newAmt);
                } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Invalid amount"); }
            }
        });

        // Load data and start the listener
        startBalanceListener(frame.getCurrentUserEmail());
        loadGroups(frame.getCurrentUserEmail());
    }

    private void startBalanceListener(String email) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("users").document(email).addSnapshotListener((snapshot, e) -> {
            if (snapshot != null && snapshot.exists()) {
                Double cloudBal = snapshot.getDouble("balance");
                this.balance = (cloudBal != null) ? cloudBal : 0.0;
                SwingUtilities.invokeLater(() ->
                        balanceLabel.setText(String.format("$%.2f", balance)));
            }
        });
    }

    private void loadGroups(String email) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            var query = db.collection("groups")
                    .whereArrayContains("approvedMembers", email)
                    .get().get();

            groupListPanel.removeAll();
            groupListPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbcList = new GridBagConstraints();
            gbcList.gridx = 0; gbcList.gridy = 0;
            gbcList.weightx = 1.0;
            gbcList.fill = GridBagConstraints.HORIZONTAL;
            gbcList.insets = new Insets(5, 10, 5, 10);

            for (QueryDocumentSnapshot doc : query.getDocuments()) {
                String gId = doc.getId();
                String gName = doc.getString("groupName");
                JPanel groupItem = new JPanel(new BorderLayout());
                groupItem.setBackground(Color.WHITE);
                groupItem.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                groupItem.setPreferredSize(new Dimension(0, 50));
                groupItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel nameLabel = new JLabel("  " + gName);
                nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                groupItem.add(nameLabel, BorderLayout.CENTER);

                groupItem.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        frame.showGroupDashboard(gId);
                    }
                });
                groupListPanel.add(groupItem, gbcList);
                gbcList.gridy++;
            }
            groupListPanel.revalidate();
            groupListPanel.repaint();
        } catch (Exception e) { e.printStackTrace(); }
    }
}