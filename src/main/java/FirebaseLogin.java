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

            String storedPass = doc.getString("password");

            if (doc.exists() && doc.getString("password").equals(password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                frame.showUserHome(email);

            }

            else {
                JOptionPane.showMessageDialog(frame, "Wrong password");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}