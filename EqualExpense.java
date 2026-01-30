import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualExpense extends Expense {
    public EqualExpense(String desc, double amt, User payer) {
        super(desc, amt, payer);
    }

    @Override
    public Map<User, Double> calculateSplits(List<User> members) {
        Map<User, Double> splits = new HashMap<>();
        double share = getAmount() / members.size();
        for (User u : members) splits.put(u, share);
        return splits;
    }
}
