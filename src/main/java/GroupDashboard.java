import javax.swing.*;
import java.awt.*;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import java.util.List;

public class GroupDashboard extends JPanel {
    private String groupId;
    private AppFrame frame;
    private JPanel historyPanel;
    private JPanel unpaidPanel;
    private JLabel nameLabel;
    private JButton adminBtn;

    public GroupDashboard(AppFrame frame, String groupId) {
        this.frame = frame;
        this.groupId = groupId;
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230));

        initUI();
        loadData();
    }

    private void initUI() {
        // --- TOP BAR (Back, Name, Members, Admin) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftTop.setOpaque(false);
        JButton backBtn = new JButton("< Back");
        backBtn.addActionListener(e -> frame.showUserHome(frame.getCurrentUserEmail()));
        nameLabel = new JLabel("Loading...");
        nameLabel.setPreferredSize(new Dimension(150, 30));
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftTop.add(backBtn); leftTop.add(nameLabel);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightTop.setOpaque(false);
        JButton membersBtn = new JButton("members");
        adminBtn = new JButton("admin role");
        adminBtn.setForeground(Color.RED);
        adminBtn.setVisible(false);
        adminBtn.addActionListener(e -> frame.showAdminRole(groupId));
        rightTop.add(membersBtn); rightTop.add(adminBtn);

        topBar.add(leftTop, BorderLayout.WEST);
        topBar.add(rightTop, BorderLayout.EAST);

        // --- CENTER: UNPAID EXPENSES (Split Logic) ---
        unpaidPanel = new JPanel();
        unpaidPanel.setLayout(new BoxLayout(unpaidPanel, BoxLayout.Y_AXIS));
        unpaidPanel.setOpaque(false);
        unpaidPanel.setBorder(BorderFactory.createTitledBorder("Unpaid - Your Share"));

        // --- SOUTH: HISTORY ---
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(historyPanel);
        scroll.setPreferredSize(new Dimension(400, 200));
        scroll.setBorder(BorderFactory.createTitledBorder("Paid History"));

        add(topBar, BorderLayout.NORTH);
        add(unpaidPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);
    }

    private void loadData() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentSnapshot groupDoc = db.collection("groups").document(groupId).get().get();

            if (groupDoc.exists()) {
                nameLabel.setText(groupDoc.getString("groupName"));

                // Member count for split calculation
                List<String> members = (List<String>) groupDoc.get("approvedMembers");
                int memberCount = (members != null && !members.isEmpty()) ? members.size() : 1;

                // Admin visibility
                if (frame.getCurrentUserEmail().equals(groupDoc.getString("admin"))) {
                    adminBtn.setVisible(true);
                }

                // --- 1. HANDLE UNPAID EXPENSES ---
                QuerySnapshot unpaidSnap = db.collection("groups").document(groupId)
                        .collection("expenses").get().get();

                unpaidPanel.removeAll();
                if (unpaidSnap.isEmpty()) {
                    JLabel noUnpaid = new JLabel("No expense to be paid");
                    noUnpaid.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                    noUnpaid.setForeground(Color.GRAY);
                    unpaidPanel.add(noUnpaid);
                } else {
                    for (QueryDocumentSnapshot doc : unpaidSnap.getDocuments()) {
                        double total = doc.getDouble("amount");
                        double share = total / memberCount;
                        addExpenseRow(unpaidPanel, doc.getString("title"), share, true);
                    }
                }

                // --- 2. HANDLE PAID HISTORY ---
                QuerySnapshot historySnap = db.collection("groups").document(groupId)
                        .collection("history").get().get();

                historyPanel.removeAll();
                if (historySnap.isEmpty()) {
                    // This is the message from your second picture
                    JLabel noHistory = new JLabel("No history of expenses found.");
                    noHistory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    noHistory.setForeground(new Color(150, 150, 150));
                    noHistory.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                    historyPanel.add(noHistory);
                } else {
                    for (QueryDocumentSnapshot doc : historySnap.getDocuments()) {
                        addExpenseRow(historyPanel, doc.getString("title"), doc.getDouble("amount"), false);
                    }
                }
            }
            revalidate();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addExpenseRow(JPanel panel, String title, double amount, boolean isUnpaid) {
        JPanel row = new JPanel(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Formatting the amount for symmetry
        JLabel amt = new JLabel(String.format("$%.2f", amount));
        amt.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Red for what you owe, Green for what was already paid (History)
        amt.setForeground(isUnpaid ? new Color(231, 76, 60) : new Color(46, 204, 113));

        row.add(lbl, BorderLayout.WEST);
        row.add(amt, BorderLayout.EAST);
        panel.add(row);
        // Add a small gap between rows
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
}