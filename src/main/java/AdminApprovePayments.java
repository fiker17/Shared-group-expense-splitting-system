import javax.swing.*;
import java.awt.*;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import java.util.List;

public class AdminApprovePayments extends JPanel {
    private String groupId;
    private JPanel listPanel;
    private AppFrame frame;

    public AdminApprovePayments(AppFrame frame, String groupId) {
        this.frame = frame;
        this.groupId = groupId;
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Approve Payments", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(45, 52, 54));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        listPanel = new JPanel(new GridBagLayout());
        listPanel.setBackground(Color.WHITE);

        loadExpenses();

        JButton back = new JButton("Back");
        back.addActionListener(e -> frame.showAdminRole(groupId));

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }

    private void loadExpenses() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            QuerySnapshot expenseSnap = db.collection("groups").document(groupId).collection("expenses").get().get();

            listPanel.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.insets = new Insets(5, 10, 5, 10);

            List<QueryDocumentSnapshot> docs = expenseSnap.getDocuments();

            if (docs.isEmpty()) {
                listPanel.add(new JLabel("No payments to approve."), gbc);
            } else {
                for (QueryDocumentSnapshot doc : docs) {
                    JPanel item = new JPanel(new BorderLayout());
                    item.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                    item.setBackground(Color.WHITE);
                    item.setPreferredSize(new Dimension(0, 45));

                    JLabel info = new JLabel("  " + doc.getString("title") + " - $" + doc.get("amount"));
                    item.add(info, BorderLayout.CENTER);

                    JButton paidBtn = new JButton("Mark Paid");
                    styleBoxButton(paidBtn, new Color(46, 204, 113)); // Green
                    paidBtn.addActionListener(e -> markAsPaid(doc.getId()));

                    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 7));
                    btnPanel.setOpaque(false);
                    btnPanel.add(paidBtn);
                    item.add(btnPanel, BorderLayout.EAST);

                    listPanel.add(item, gbc);
                    gbc.gridy++;
                }
            }
            gbc.weighty = 1.0;
            listPanel.add(new JLabel(""), gbc);
            listPanel.revalidate(); listPanel.repaint();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void styleBoxButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btn.setPreferredSize(new Dimension(100, 30));
    }

    private void markAsPaid(String expenseId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Mark this expense as fully paid?", "Confirm Payment", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Firestore db = FirestoreClient.getFirestore();
                DocumentReference expenseRef = db.collection("groups").document(groupId)
                        .collection("expenses").document(expenseId);

                // 1. Get the data from the current expense
                DocumentSnapshot snapshot = expenseRef.get().get();
                if (snapshot.exists()) {
                    // 2. Copy it to the 'history' sub-collection
                    db.collection("groups").document(groupId)
                            .collection("history").document(expenseId).set(snapshot.getData());

                    // 3. Now delete it from the 'expenses' sub-collection
                    expenseRef.delete();

                    JOptionPane.showMessageDialog(this, "Expense moved to History!");
                    loadExpenses(); // Refresh the admin list
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing payment.");
            }
        }
    }
}