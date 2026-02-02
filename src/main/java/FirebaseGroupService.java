import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.*;

public class FirebaseGroupService {

    // =============================
    // Create Group (With ID Check)
    // =============================
    public static boolean createGroup(Group group) {

        try {
            Firestore db = FirestoreClient.getFirestore();

            // Check if group already exists
            var doc = db.collection("groups")
                    .document(group.getGroupId())
                    .get().get();

            if (doc.exists()) {
                return false; // ID already used
            }

            Map<String, Object> data = new HashMap<>();

            data.put("groupName", group.getGroupName());
            data.put("admin", group.getAdminEmail());

            // Owner is first approved member
            List<String> approved = new ArrayList<>();
            approved.add(group.getAdminEmail());

            data.put("approvedMembers", approved);
            data.put("pendingMembers", new ArrayList<>());

            db.collection("groups")
                    .document(group.getGroupId())
                    .set(data);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // =============================
    // Add Expense
    // =============================
    public static void addExpense(String groupId, Expense expense) {

        try {
            Firestore db = FirestoreClient.getFirestore();

            Map<String, Object> data = new HashMap<>();

            data.put("title", expense.getTitle());
            data.put("amount", expense.getAmount());
            data.put("paidBy", expense.getPaidBy());

            db.collection("groups")
                    .document(groupId)
                    .collection("expenses")
                    .add(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // =============================
    // Get Groups For User
    // =============================
    public static List<String> getUserGroups(String email) {

        List<String> groups = new ArrayList<>();

        try {
            Firestore db = FirestoreClient.getFirestore();

            var snapshot =
                    db.collection("groups").get().get();

            for (var doc : snapshot.getDocuments()) {

                List<String> approved =
                        (List<String>) doc.get("approvedMembers");

                if (approved != null && approved.contains(email)) {
                    groups.add(doc.getId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return groups;
    }


    // =============================
    // Get Group Info
    // =============================
    public static Map<String, Object> getGroup(String groupId) {

        try {
            Firestore db = FirestoreClient.getFirestore();

            var doc =
                    db.collection("groups")
                            .document(groupId)
                            .get().get();

            if (doc.exists()) {
                return doc.getData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // =============================
    // Approve Member (Admin Only)
    // =============================
    public static void approveMember(
            String groupId,
            String adminEmail,
            String userEmail
    ) {

        try {
            Firestore db = FirestoreClient.getFirestore();

            var ref =
                    db.collection("groups").document(groupId);

            var doc = ref.get().get();

            if (!doc.exists()) return;

            // Check admin
            if (!adminEmail.equals(doc.getString("admin")))
                return;

            List<String> pending =
                    (List<String>) doc.get("pendingMembers");

            List<String> approved =
                    (List<String>) doc.get("approvedMembers");

            if (pending == null || approved == null)
                return;

            if (pending.contains(userEmail)) {

                pending.remove(userEmail);
                approved.add(userEmail);

                ref.update("pendingMembers", pending);
                ref.update("approvedMembers", approved);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // =============================
// Get Pending Members
// =============================
    public static List<String> getPendingMembers(String groupId) {

        try {
            Firestore db = FirestoreClient.getFirestore();

            var doc =
                    db.collection("groups")
                            .document(groupId)
                            .get().get();

            if (!doc.exists()) return null;

            return (List<String>) doc.get("pendingMembers");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
