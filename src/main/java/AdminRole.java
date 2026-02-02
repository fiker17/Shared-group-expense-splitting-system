import javax.swing.*;
import java.awt.*;

public class AdminRole extends JPanel {
    private String groupId;
    private AppFrame frame;

    public AdminRole(AppFrame frame, String groupId) {
        this.frame = frame;
        this.groupId = groupId;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Admin Control Panel");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JButton approveBtn = new JButton("1. Approve Members");
        JButton addExpenseBtn = new JButton("2. Add New Expense");
        JButton approvePaymentsBtn = new JButton("3. Approve Payments");
        JButton removeMembersBtn = new JButton("4. Remove Members");
        JButton backBtn = new JButton("Back to Dashboard");

        styleButton(approveBtn);
        styleButton(addExpenseBtn);
        styleButton(approvePaymentsBtn);
        styleButton(removeMembersBtn);

        gbc.gridy = 0; add(title, gbc);
        gbc.gridy++; add(approveBtn, gbc);
        gbc.gridy++; add(addExpenseBtn, gbc);
        gbc.gridy++; add(approvePaymentsBtn, gbc);
        gbc.gridy++; add(removeMembersBtn, gbc);
        gbc.gridy++; add(backBtn, gbc);

        // Navigation
        approveBtn.addActionListener(e -> showSubPanel(new AdminApproveMembers(frame, groupId)));
        addExpenseBtn.addActionListener(e -> showSubPanel(new AdminAddExpense(frame, groupId)));
        approvePaymentsBtn.addActionListener(e -> showSubPanel(new AdminApprovePayments(frame, groupId)));
        removeMembersBtn.addActionListener(e -> showSubPanel(new AdminRemoveMembers(frame, groupId)));
        backBtn.addActionListener(e -> frame.showGroupDashboard(groupId));
    }

    private void styleButton(JButton btn) {
        btn.setPreferredSize(new Dimension(250, 40));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void showSubPanel(JPanel panel) {
        removeAll();
        setLayout(new BorderLayout());
        add(panel);
        revalidate();
        repaint();
    }
}