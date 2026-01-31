import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import javax.swing.JOptionPane;

public class FirebaseLogin {
    public static void authenticate(String email, String password, AppFrame frame) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            var doc = db.collection("users").document(email).get().get();
            
            if (doc.exists() && doc.getString("password").equals(password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
