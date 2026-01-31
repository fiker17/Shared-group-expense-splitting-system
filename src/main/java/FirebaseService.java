import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;

public class FirebaseService {

    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;

        try {
            FileInputStream serviceAccount =
                    new FileInputStream("firebase-key.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            initialized = true;

            System.out.println("Firebase initialized");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}