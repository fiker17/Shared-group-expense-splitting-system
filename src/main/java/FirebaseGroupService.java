import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.HashMap;
import java.util.Map;

public class FirebaseGroupService {

    public static void createGroup(Group group) {
        try {
            Firestore db = FirestoreClient.getFirestore();

            Map<String, Object> data = new HashMap<>();
            data.put("groupName", group.getGroupName());
            data.put("admin", group.getAdminEmail());
            data.put("approvedMembers", group.getApprovedMembers());
            data.put("pendingMembers", group.getPendingMembers());

            db.collection("groups").document(group.getGroupId()).set(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addExpense(String groupId, Expense expense) {
        try {
            Firestore db = FirestoreClient.getFirestore();

            Map<String, Object> data = new HashMap<>();
            data.put("title", expense.getTitle());
            data.put("amount", expense.getAmount());

            db.collection("groups")
                    .document(groupId)
                    .collection("expenses")
                    .add(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}