import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import java.util.ArrayList;
import java.util.List;

public class FirebaseJoinGroup {

    public static boolean requestJoin(String groupId, String email) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            var docRef = db.collection("groups").document(groupId);
            var doc = docRef.get().get();

            // 1. Check if the group actually exists in the database
            if (!doc.exists()) {
                return false;
            }

            // 2. Get the current list of pending members
            List<String> pending = (List<String>) doc.get("pendingMembers");
            if (pending == null) {
                pending = new ArrayList<>();
            }

            // 3. Add the user only if they aren't already in the pending list
            if (!pending.contains(email)) {
                pending.add(email);
                docRef.update("pendingMembers", pending);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}