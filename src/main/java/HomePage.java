import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {

    public HomePage(AppFrame frame, String userEmail) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcome = new JLabel("Welcome, " + userEmail);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton createGroupBtn = new JButton("Create Group");
        JButton joinGroupBtn = new JButton("Join Group");

        JTextField groupIdField = new JTextField(15);

        gbc.gridy = 0;
        add(welcome, gbc);

        gbc.gridy++;
        add(createGroupBtn, gbc);

        gbc.gridy++;
        add(new JLabel("Group ID:"), gbc);

        gbc.gridy++;
        add(groupIdField, gbc);

        gbc.gridy++;
        add(joinGroupBtn, gbc);

        // ACTIONS
        createGroupBtn.addActionListener(e -> {
            String groupId = JOptionPane.showInputDialog(
                    frame,
                    "Enter new Group ID:"
            );

            if (groupId != null && !groupId.isEmpty()) {
                Group group = new Group(groupId, "My Group", userEmail);
                FirebaseGroupService.createGroup(group);
                JOptionPane.showMessageDialog(frame, "Group created!");
            }
        });

        joinGroupBtn.addActionListener(e -> {
            String groupId = groupIdField.getText().trim();

            if (groupId.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter Group ID");
                return;
            }

            FirebaseJoinGroup.requestJoin(groupId, userEmail);
            JOptionPane.showMessageDialog(frame, "Join request sent!");
        });
    }
}