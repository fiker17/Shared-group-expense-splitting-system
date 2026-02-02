import javax.swing.*;
import java.awt.*;

public class AdminAddExpense extends JPanel {
    public AdminAddExpense(AppFrame frame, String groupId) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JTextField titleField = new JTextField(20);
        JTextField amountField = new JTextField(20);
        JButton submit = new JButton("Add Expense");
        JButton back = new JButton("Back");

        gbc.gridy=0; add(new JLabel("Expense Name:"), gbc);
        gbc.gridy++; add(titleField, gbc);
        gbc.gridy++; add(new JLabel("Amount ($):"), gbc);
        gbc.gridy++; add(amountField, gbc);
        gbc.gridy++; add(submit, gbc);
        gbc.gridy++; add(back, gbc);

        submit.addActionListener(e -> {
            String title = titleField.getText();
            double amount = Double.parseDouble(amountField.getText());
            FirebaseGroupService.addExpense(groupId, new Expense(title, amount));
            JOptionPane.showMessageDialog(this, "Expense Added!");
            frame.showAdminRole(groupId);
        });

        back.addActionListener(e -> frame.showAdminRole(groupId));
    }
}