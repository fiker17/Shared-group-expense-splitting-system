import java.util.ArrayList;
import java.util.List;

public class Group {

    private String groupId;
    private String groupName;
    private String adminEmail;

    private List<String> approvedMembers;
    private List<String> pendingMembers;
    private List<Expense> expenses;


    // =============================
    // Normal Constructor (Create Group)
    // =============================
    public Group(String groupId,
                 String groupName,
                 String adminEmail) {

        this.groupId = groupId;
        this.groupName = groupName;
        this.adminEmail = adminEmail;

        approvedMembers = new ArrayList<>();
        pendingMembers = new ArrayList<>();
        expenses = new ArrayList<>();

        // Admin is automatically approved
        approvedMembers.add(adminEmail);
    }


    // =============================
    // Firebase Constructor (Load Group)
    // =============================
    public Group(String groupId,
                 String groupName,
                 String adminEmail,
                 List<String> members) {

        this.groupId = groupId;
        this.groupName = groupName;
        this.adminEmail = adminEmail;

        this.approvedMembers = members != null
                ? members
                : new ArrayList<>();

        this.pendingMembers = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }


    // ===== MEMBER MANAGEMENT =====

    public void requestToJoin(String email) {
        if (!pendingMembers.contains(email)
                && !approvedMembers.contains(email)) {

            pendingMembers.add(email);
        }
    }


    public void approveMember(String email) {

        if (pendingMembers.contains(email)) {

            pendingMembers.remove(email);
            approvedMembers.add(email);
        }
    }


    // ===== EXPENSE MANAGEMENT =====

    public void addExpense(String title,
                           double amount,
                           String paidBy) {

        expenses.add(new Expense(title, amount, paidBy));
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
