import javax.swing.*;
import java.awt.*;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import java.util.List;

public class AdminApproveMembers extends JPanel {
    private String groupId;
    private JPanel listPanel;
    private AppFrame frame;

    public AdminApproveMembers(AppFrame frame, String groupId) {
        this.frame = frame;
        this.groupId = groupId;
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Approve Join Requests", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(45, 52, 54));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        listPanel = new JPanel(new GridBagLayout());
        listPanel.setBackground(Color.WHITE);

        loadPending();

        JButton back = new JButton("Back");
        back.addActionListener(e -> frame.showAdminRole(groupId));

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }

    private void loadPending() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentSnapshot doc = db.collection("groups").document(groupId).get().get();
            List<String> pending = (List<String>) doc.get("pendingMembers");

            listPanel.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.insets = new Insets(5, 10, 5, 10);

            if (pending == null || pending.isEmpty()) {
                listPanel.add(new JLabel("No pending requests."), gbc);
            } else {
                for (String email : pending) {
                    JPanel item = new JPanel(new BorderLayout());
                    item.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                    item.setBackground(Color.WHITE);
                    item.setPreferredSize(new Dimension(0, 45));

                    item.add(new JLabel("  " + email), BorderLayout.CENTER);

                    JButton accept = new JButton("Accept");
                    styleBoxButton(accept, new Color(46, 204, 113)); // Green
                    accept.addActionListener(e -> approve(email));

                    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 7));
                    btnPanel.setOpaque(false);
                    btnPanel.add(accept);
                    item.add(btnPanel, BorderLayout.EAST);

                    listPanel.add(item, gbc);
                    gbc.gridy++;
                }
            }
            addSpacer(gbc);
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

    private void addSpacer(GridBagConstraints gbc) {
        gbc.weighty = 1.0;
        listPanel.add(new JLabel(""), gbc);
    }

    private void approve(String email) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference ref = db.collection("groups").document(groupId);
            ref.update("approvedMembers", FieldValue.arrayUnion(email));
            ref.update("pendingMembers", FieldValue.arrayRemove(email));
            loadPending();
        } catch (Exception e) { e.printStackTrace(); }
    }
}