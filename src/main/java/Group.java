import java.util.ArrayList;
import java.util.List;

public class Group {

    private String groupId;
    private String groupName;
    private String adminEmail;

    private List<String> approvedMembers;
    private List<String> pendingMembers;
    private List<Expense> expenses;

    public Group(String groupId, String groupName, String adminEmail) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.adminEmail = adminEmail;

        approvedMembers = new ArrayList<>();
        pendingMembers = new ArrayList<>();
        expenses = new ArrayList<>();

        // Admin is automatically a member
        approvedMembers.add(adminEmail);
    }

    // ===== MEMBER MANAGEMENT =====

    public void requestToJoin(String email) {
        pendingMembers.add(email);
    }

    public void approveMember(String email) {
        for (int i = 0; i < pendingMembers.size(); i++) {
            if (pendingMembers.get(i).equals(email)) {
                approvedMembers.add(email);
                pendingMembers.remove(i);
                break;
            }
        }
    }

    // ===== EXPENSE MANAGEMENT =====

    public void addExpense(String title, double amount) {
        expenses.add(new Expense(title, amount));
    }

    // ===== GETTERS =====

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public List<String> getApprovedMembers() {
        return approvedMembers;
    }

    public List<String> getPendingMembers() {
        return pendingMembers;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}