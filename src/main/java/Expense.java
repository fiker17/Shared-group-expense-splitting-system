public class Expense {

    private String title;
    private double amount;

    public Expense(String title, double amount) {
        this.title = title;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }
}
