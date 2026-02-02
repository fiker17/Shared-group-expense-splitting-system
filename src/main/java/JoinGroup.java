import javax.swing.*;
import java.awt.*;

public class JoinGroup extends JPanel {

    public JoinGroup(AppFrame frame, String userEmail) {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);

        JTextField groupIdField = new JTextField(15);

        JButton joinBtn = new JButton("Request Join");
        JButton backBtn = new JButton("Back");

        gbc.gridy = 0;
        add(new JLabel("Join Group"), gbc);

        gbc.gridy++;
        add(new JLabel("Group ID:"), gbc);

        gbc.gridy++;
        add(groupIdField, gbc);

        gbc.gridy++;
        add(joinBtn, gbc);

        gbc.gridy++;
        add(backBtn, gbc);

        joinBtn.addActionListener(e -> {

            String groupId = groupIdField.getText().trim();

            if (groupId.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Enter Group ID");
                return;
            }

            boolean success =
                    FirebaseJoinGroup.requestJoin(groupId, userEmail);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Join request sent");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cannot join group");
            }
        });

        backBtn.addActionListener(e ->
                frame.showUserHome(userEmail)
        );
    }
}
