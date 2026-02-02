import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class FirebaseRegister {

    public static void registerUser(
            String username,
            String email,
            String password,
            AppFrame frame
    ) {
        try {
            Firestore db = FirestoreClient.getFirestore();

            // Check if user already exists
            if (db.collection("users").document(email).get().get().exists()) {
                JOptionPane.showMessageDialog(frame, "User already exists");
                return;
            }

            Map<String, Object> user = new HashMap<>();
            user.put("username", username);
            user.put("email", email);
            user.put("password", password);

            db.collection("users").document(email).set(user);

            JOptionPane.showMessageDialog(frame, "Registered successfully!");
            frame.showStartHome();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error registering user");
        }
    }
}