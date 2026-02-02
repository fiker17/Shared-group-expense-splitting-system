import javax.swing.*;
import java.awt.*;

public class StartHome extends JPanel {

    public StartHome(AppFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.setPreferredSize(new Dimension(200, 45));
        registerBtn.setPreferredSize(new Dimension(200, 45));

        gbc.gridy = 0;
        add(loginBtn, gbc);

        gbc.gridy = 1;
        add(registerBtn, gbc);

        loginBtn.addActionListener(e -> frame.showView("LOGIN"));
        registerBtn.addActionListener(e -> frame.showView("REGISTER"));
    }
}