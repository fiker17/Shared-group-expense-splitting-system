import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;

public class FirebaseJoinGroup {

    public static boolean requestJoin(String groupId, String email) {

        try {
            Firestore db = FirestoreClient.getFirestore();

            var ref =
                    db.collection("groups").document(groupId);

            var doc = ref.get().get();

            // Group not found
            if (!doc.exists()) return false;

            List<String> pending =
                    (List<String>) doc.get("pendingMembers");

            List<String> approved =
                    (List<String>) doc.get("approvedMembers");

            if (pending == null)
                pending = new ArrayList<>();

            // Already member
            if (approved != null && approved.contains(email))
                return false;

            // Already requested
            if (pending.contains(email))
                return false;

            pending.add(email);

            ref.update("pendingMembers", pending);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
