import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private String code;
    private User admin;
    private int capacity;
    private List<User> members = new ArrayList<>();
    private List<User> pending = new ArrayList<>();
    private List<Expense> expenses = new ArrayList<>();

    public Group(String name, String code, User admin, int capacity) {
        this.name = name;
        this.code = code;
        this.admin = admin;
        this.capacity = capacity;
        this.members.add(admin);
    }

    public void setCapacity(int n) { this.capacity = n; }
    public String getCode() { return code; }
    public User getAdmin() { return admin; }
    public List<User> getMembers() { return members; }
    public List<User> getPending() { return pending; }
    public List<Expense> getExpenses() { return expenses; }

    public void addExpense(Expense e) { expenses.add(e); }
    public void addPending(User u) { if (members.size() < capacity) pending.add(u); }
}
