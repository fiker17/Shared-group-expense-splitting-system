import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GroupPage extends JPanel {

    private Group group;
    private String userEmail;
    private AppFrame frame;

    public GroupPage(
            AppFrame frame,
            Group group,
            String userEmail
    ) {

        this.frame = frame;
        this.group = group;
        this.userEmail = userEmail;

        setLayout(new BorderLayout());

        // ================= TOP =================

        JPanel top = new JPanel(new BorderLayout());

        JLabel nameLabel =
                new JLabel(group.getGroupName());

        nameLabel.setFont(new Font("Arial",
                Font.BOLD, 18));

        JButton membersBtn = new JButton("Members");

        JPanel right = new JPanel();
        right.add(membersBtn);

        // Admin button
        if (userEmail.equals(group.getAdminEmail())) {

            JButton adminBtn = new JButton("Admin");
            right.add(adminBtn);

            adminBtn.addActionListener(e ->
                    showPendingMembers()
            );
        }

        top.add(nameLabel, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);


        // ================= CENTER =================

        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText("Expenses coming soon...");

        add(new JScrollPane(area),
                BorderLayout.CENTER);


        // ================= BOTTOM =================

        JButton backBtn = new JButton("Back");

        backBtn.addActionListener(e ->
                frame.showUserHome(userEmail)
        );

        add(backBtn, BorderLayout.SOUTH);


        // ================= MEMBERS BUTTON =================

        membersBtn.addActionListener(e -> {

            List<String> members =
                    group.getApprovedMembers();

            if (members == null || members.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No members yet"
                );
                return;
            }

            StringBuilder sb = new StringBuilder();

            for (String m : members) {
                sb.append(m).append("\n");
            }

            JOptionPane.showMessageDialog(
                    this,
                    sb.toString(),
                    "Members",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }


    // =============================
    // Admin Panel
    // =============================
    private void showPendingMembers() {

        List<String> pending =
                FirebaseGroupService
                        .getPendingMembers(
                                group.getGroupId()
                        );

        if (pending == null || pending.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No pending requests"
            );
            return;
        }

        String selected =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Approve user:",
                        "Admin Panel",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        pending.toArray(),
                        null
                );

        if (selected != null) {

            FirebaseGroupService.approveMember(
                    group.getGroupId(),
                    userEmail,
                    selected
            );

            JOptionPane.showMessageDialog(
                    this,
                    "User approved"
            );
        }
    }
}
