import javax.swing.*;
import java.awt.*;

public class CreateGroup extends JPanel {
    private String userEmail;
    private AppFrame frame;


    public CreateGroup(AppFrame frame, String userEmail) {
        this.frame = frame;
        this.userEmail = userEmail;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JLabel title = new JLabel("Create a group");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JTextField groupName = new JTextField(20);
        JTextField groupId = new JTextField(20);

        JButton createBtn = new JButton("Create");
        JButton backBtn = new JButton("Back");

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy++;
        add(groupName, gbc);

        gbc.gridy++;
        add(groupId, gbc);

        gbc.gridy++;
        add(createBtn, gbc);

        gbc.gridy++;
        add(backBtn, gbc);

        createBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Group created successfully");
            frame.showUserHome(null); // or keep username if stored
        });

        backBtn.addActionListener(e -> frame.showUserHome(null));
    }
}