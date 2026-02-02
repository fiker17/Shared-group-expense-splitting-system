import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import javax.swing.JOptionPane;

public class FirebaseLogin {
    public static void authenticate(String email, String password, AppFrame frame) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            var doc = db.collection("users").document(email).get().get();

            if (!doc.exists()) {
                JOptionPane.showMessageDialog(frame, "User not found");
                return;
            }

            if (doc.getString("password").equals(password)) {
                String dbUsername = doc.getString("username"); // Get actual name
                frame.setCurrentUserEmail(email); // Save email for logic
                JOptionPane.showMessageDialog(frame, "Welcome " + dbUsername);
                frame.showUserHome(dbUsername); // Pass name to UI
            } else {
                JOptionPane.showMessageDialog(frame, "Wrong password");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}