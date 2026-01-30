import java.util.*;

public abstract class Expense {
    private String description;
    private double amount;
    private User payer;

    public Expense(String description, double amount, User payer) {
        this.description = description;
        this.amount = amount;
        this.payer = payer;
    }

    public abstract Map<User, Double> calculateSplits(List<User> members);

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public User getPayer() {
        return payer;
    }
}