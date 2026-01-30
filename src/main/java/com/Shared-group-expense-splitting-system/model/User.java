public class User {
    private String username;
    private String password;
    private double initialBalance;

    // Constructor with username and password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.initialBalance = 0.0;
    }

    // Constructor with initial balance
    public User(String username, String password, double initialBalance) {
        this.username = username;
        this.password = password;
        this.initialBalance = initialBalance;
    }

    public String getUsername() {
        return username; }
    public boolean checkPassword(String pass) {
        return password.equals(pass); }
    public double getInitialBalance() {
        return initialBalance; }
    public void setInitialBalance(double balance) {
        this.initialBalance = balance; }

    @Override
    public String toString() {
        return username; }
}