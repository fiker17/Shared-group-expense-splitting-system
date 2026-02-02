import javax.swing.*;
import java.awt.*;

public class StartHome extends JPanel {

    public StartHome(AppFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel title = new JLabel("Welcome to Shared Expense App");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel subtitle = new JLabel("Please login or register to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.setPreferredSize(new Dimension(200, 45));
        registerBtn.setPreferredSize(new Dimension(200, 45));

        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy = 1;
        add(subtitle, gbc);

        gbc.gridy = 2;
        add(loginBtn, gbc);

        gbc.gridy = 3;
        add(registerBtn, gbc);

        loginBtn.addActionListener(e -> frame.showView("LOGIN"));
        registerBtn.addActionListener(e -> frame.showView("REGISTER"));
    }
}