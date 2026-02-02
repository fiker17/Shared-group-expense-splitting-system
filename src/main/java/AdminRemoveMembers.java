import javax.swing.*;
import java.awt.*;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import java.util.List;

public class AdminRemoveMembers extends JPanel {
    private String groupId;
    private JPanel listPanel;
    private AppFrame frame;

    public AdminRemoveMembers(AppFrame frame, String groupId) {
        this.frame = frame;
        this.groupId = groupId;
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Remove Members", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(45, 52, 54));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        listPanel = new JPanel(new GridBagLayout());
        listPanel.setBackground(Color.WHITE);

        loadMembers();

        JButton back = new JButton("Back");
        back.addActionListener(e -> frame.showAdminRole(groupId));

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }

    private void loadMembers() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentSnapshot doc = db.collection("groups").document(groupId).get().get();
            List<String> members = (List<String>) doc.get("approvedMembers");
            String admin = doc.getString("admin");

            listPanel.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.insets = new Insets(5, 10, 5, 10);

            for (String email : members) {
                if (email.equals(admin)) continue;

                JPanel item = new JPanel(new BorderLayout());
                item.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                item.setBackground(Color.WHITE);
                item.setPreferredSize(new Dimension(0, 45));

                item.add(new JLabel("  " + email), BorderLayout.CENTER);

                JButton remove = new JButton("Remove");
                styleBoxButton(remove, new Color(231, 76, 60)); // Red
                remove.addActionListener(e -> removeUser(email));

                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 7));
                btnPanel.setOpaque(false);
                btnPanel.add(remove);
                item.add(btnPanel, BorderLayout.EAST);

                listPanel.add(item, gbc);
                gbc.gridy++;
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

    private void removeUser(String email) {
        if (JOptionPane.showConfirmDialog(this, "Remove " + email + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                FirestoreClient.getFirestore().collection("groups").document(groupId)
                        .update("approvedMembers", FieldValue.arrayRemove(email));
                loadMembers();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}