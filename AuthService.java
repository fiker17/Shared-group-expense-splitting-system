import java.util.*;

public class AuthService {
    private Map<String, User> users = new HashMap<>();

    public void register(String username, String password, double balance) {
        users.put(username, new User(username, password, balance));
    }

    public User login(String username, String password) {
        if (users.containsKey(username) &&
            users.get(username).checkPassword(password)) {
            return users.get(username);
        }
        return null;
    }

    public User getUser(String username) {
        return users.get(username);
    }
}
