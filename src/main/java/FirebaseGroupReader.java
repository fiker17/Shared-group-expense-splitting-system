import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;

public class FirebaseGroupReader {

    public static List<Group> getUserGroups(String email) {

        List<Group> groups = new ArrayList<>();

        try {
            Firestore db = FirestoreClient.getFirestore();

            var docs = db.collection("groups").get().get();

            for (var doc : docs) {

                List<String> members =
                        (List<String>) doc.get("approvedMembers");

                String admin = doc.getString("admin");

                if ((members != null && members.contains(email))
                        || email.equals(admin)) {

                    Group g = new Group(
                            doc.getId(),
                            doc.getString("groupName"),
                            admin,
                            members
                    );

                    groups.add(g);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return groups;
    }
}
