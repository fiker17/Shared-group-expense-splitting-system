import javax.swing.*;
import java.awt.*;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class CreateGroup extends JPanel {

    public CreateGroup(AppFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JLabel title = new JLabel("Create a group");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        // Use proper variable names for text fields
        JTextField groupNameField = new JTextField("Group Name", 20);
        JTextField groupIdField = new JTextField("Group ID", 20);
        groupNameField.setForeground(Color.GRAY);
        groupIdField.setForeground(Color.GRAY);

        // Simple placeholder logic
        setupPlaceholder(groupNameField, "Group Name");
        setupPlaceholder(groupIdField, "Group ID");

        JButton createBtn = new JButton("Create");
        JButton backBtn = new JButton("Back");

        gbc.gridy = 0; add(title, gbc);
        gbc.gridy++; add(groupNameField, gbc);
        gbc.gridy++; add(groupIdField, gbc);
        gbc.gridy++; add(createBtn, gbc);
        gbc.gridy++; add(backBtn, gbc);

        createBtn.addActionListener(e -> {
            String name = groupNameField.getText().trim();
            String id = groupIdField.getText().trim();
            String admin = frame.getCurrentUserEmail(); // No longer null

            if (id.isEmpty() || id.equals("Group ID")) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid Group ID");
                return;
            }

            try {
                Firestore db = FirestoreClient.getFirestore();
                // Check if ID is unique
                if (db.collection("groups").document(id).get().get().exists()) {
                    JOptionPane.showMessageDialog(frame, "ID already exists!");
                    return;
                }

                Group group = new Group(id, name, admin);
                FirebaseGroupService.createGroup(group);
                JOptionPane.showMessageDialog(frame, "Group created successfully");
                frame.showUserHome(admin);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        backBtn.addActionListener(e -> frame.showUserHome(frame.getCurrentUserEmail()));
    }

    private void setupPlaceholder(JTextField field, String text) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }
}