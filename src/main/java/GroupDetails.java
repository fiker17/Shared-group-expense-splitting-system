import javax.swing.*;
import java.awt.*;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class GroupDetails extends JPanel {
    public GroupDetails(AppFrame frame, String groupId) {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        try {
            Firestore db = FirestoreClient.getFirestore();
            var doc = db.collection("groups").document(groupId).get().get();

            JLabel nameLabel = new JLabel(doc.getString("groupName"));
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            leftTop.add(nameLabel);

            // Admin Role button (Top Left) - Only visible to creator
            if (frame.getCurrentUserEmail().equals(doc.getString("admin"))) {
                JButton adminBtn = new JButton("Admin Role");
                leftTop.add(adminBtn);
            }

            // Members button (Top Right)
            JButton membersBtn = new JButton("Members");
            rightTop.add(membersBtn);

            topPanel.add(leftTop, BorderLayout.WEST);
            topPanel.add(rightTop, BorderLayout.EAST);
            add(topPanel, BorderLayout.NORTH);

            JButton backBtn = new JButton("Back");
            backBtn.addActionListener(e -> frame.showUserHome(frame.getCurrentUserEmail()));
            add(backBtn, BorderLayout.SOUTH);

        } catch (Exception e) { e.printStackTrace(); }
    }
}