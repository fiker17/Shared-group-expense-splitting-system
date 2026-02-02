import javax.swing.*;
import java.awt.*;

public class Login extends JPanel {
    public Login(AppFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JTextField emailField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginSubmit = new JButton("Login");
        JButton backBtn = new JButton("Back");

        gbc.gridy = 0; add(new JLabel("Login "), gbc);
        gbc.gridy++; add(new JLabel("Email:"), gbc);
        gbc.gridy++; add(emailField, gbc);
        gbc.gridy++; add(new JLabel("Password:"), gbc);
        gbc.gridy++; add(passField, gbc);
        gbc.gridy++; add(loginSubmit, gbc);
        gbc.gridy++; add(backBtn, gbc);

        backBtn.addActionListener(e -> frame.showStartHome());

        loginSubmit.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            FirebaseLogin.authenticate(email, password, frame);
        });
    }
}
