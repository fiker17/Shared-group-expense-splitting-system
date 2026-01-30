import java.util.*;

public class SettlementCalculator {
    // Calculate net balances in a group
    public static void viewBalances(Group g) {
        if (g == null) return;
        Map<User, Double> net = new HashMap<>();
        for (User u : g.getMembers()) net.put(u, 0.0);

        for (Expense e : g.getExpenses()) {
            Map<User, Double> splits = e.calculateSplits(g.getMembers());
            net.put(e.getPayer(), net.get(e.getPayer()) + e.getAmount());
            for (User u : splits.keySet()) net.put(u, net.get(u) - splits.get(u));
        }

        System.out.println("\n--- Settlement Report ---");
        net.forEach((u, b) -> {
            double totalBalance = u.getInitialBalance() + b;
            System.out.println(u + ": " + (totalBalance >= 0 ? "net balance $" : "net balance -$")
                    + Math.abs(totalBalance));
        });
    }
}