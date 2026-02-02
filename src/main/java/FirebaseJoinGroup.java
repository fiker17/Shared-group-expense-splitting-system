import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;

public class FirebaseJoinGroup {

    public static void requestJoin(String groupId, String email) {
        try {
            Firestore db = FirestoreClient.getFirestore();

            var docRef = db.collection("groups").document(groupId);
            var doc = docRef.get().get();

            if (!doc.exists()) return;

            List<String> pending =
                    (List<String>) doc.get("pendingMembers");

            if (pending == null) {
                pending = new ArrayList<>();
            }

            pending.add(email);
            docRef.update("pendingMembers", pending);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}