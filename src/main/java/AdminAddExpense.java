import javax.swing.*;
import java.awt.*;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdminAddExpense extends JPanel {
    public AdminAddExpense(AppFrame frame, String groupId) {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel header = new JLabel("Add New Expense", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JTextField titleField = new JTextField(20);
        JTextField amountField = new JTextField(20);

        // STYLED BUTTON: Red Box with Black Text
        JButton submit = new JButton("Add & Deduct Balances");
        submit.setBackground(Color.RED);
        submit.setForeground(Color.BLACK);
        submit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submit.setOpaque(true);
        submit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        submit.setFocusPainted(false);

        JButton back = new JButton("Cancel");

        gbc.gridy = 0; add(header, gbc);
        gbc.gridy++; add(new JLabel("Expense Name:"), gbc);
        gbc.gridy++; add(titleField, gbc);
        gbc.gridy++; add(new JLabel("Total Amount ($):"), gbc);
        gbc.gridy++; add(amountField, gbc);
        gbc.gridy++; add(submit, gbc);
        gbc.gridy++; add(back, gbc);

        // --- ACTIONS ---
        submit.addActionListener(e -> {
            String title = titleField.getText().trim();
            String amountStr = amountField.getText().trim();

            if (title.isEmpty() || amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            try {
                double totalAmount = Double.parseDouble(amountStr);
                // Run in background thread to prevent UI freezing (Fixes ApiFuture errors)
                new Thread(() -> processExpenseAndDeduction(groupId, title, totalAmount, frame)).start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format");
            }
        });

        back.addActionListener(e -> frame.showAdminRole(groupId));
    }

    private void processExpenseAndDeduction(String groupId, String title, double totalAmount, AppFrame frame) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference groupRef = db.collection("groups").document(groupId);

            // Using .get().get() to resolve the ApiFuture correctly
            DocumentSnapshot groupDoc = groupRef.get().get();

            if (groupDoc.exists()) {
                List<String> members = (List<String>) groupDoc.get("approvedMembers");

                if (members == null || members.isEmpty()) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this, "No approved members found!"));
                    return;
                }

                double share = totalAmount / members.size();

                // 1. DEDUCT FROM ALL MEMBERS
                for (String memberEmail : members) {
                    DocumentReference userRef = db.collection("users").document(memberEmail);

                    db.runTransaction(transaction -> {
                        DocumentSnapshot userSnap = transaction.get(userRef).get();
                        Double currentBal = userSnap.getDouble("balance");
                        if (currentBal == null) currentBal = 0.0;

                        transaction.update(userRef, "balance", currentBal - share);
                        return null;
                    }).get(); // Wait for transaction completion
                }

                // 2. SAVE THE EXPENSE RECORD
                Map<String, Object> expenseData = new HashMap<>();
                expenseData.put("title", title);
                expenseData.put("amount", totalAmount);
                expenseData.put("timestamp", com.google.cloud.Timestamp.now());

                groupRef.collection("expenses").add(expenseData).get();

                // Update UI back on the Event Dispatch Thread
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Balances updated and expense recorded!");
                    frame.showAdminRole(groupId);
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this, "Firestore Error: " + ex.getMessage()));
        }
    }
}