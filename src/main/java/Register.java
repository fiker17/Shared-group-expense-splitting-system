import javax.swing.*;
import java.awt.*;

public class Register extends JPanel {
    public Register(AppFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JTextField emailField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton regSubmit = new JButton("Register");
        JButton backBtn = new JButton("Back");

        gbc.gridy = 0; add(new JLabel("Register"), gbc);
        gbc.gridy++; add(new JLabel("Email:"), gbc);
        gbc.gridy++; add(emailField, gbc);
        gbc.gridy++; add(new JLabel("Password:"), gbc);
        gbc.gridy++; add(passField, gbc);
        gbc.gridy++; add(regSubmit, gbc);
        gbc.gridy++; add(backBtn, gbc);

        backBtn.addActionListener(e -> frame.showView("HOME"));

        regSubmit.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            FirebaseRegister.registerUser(email, password, frame);
        });
    }
}
