import java.util.*;
    public class Menu {
        private static Scanner sc = new Scanner(System.in);
        private static Map<String, User> users = new HashMap<>();
        private static Map<String, Group> groups = new HashMap<>();
        private static User currentUser = null;

        public static void main(String[] args) {
            System.out.println("=== SIMPLE SHARED EXPENSE APP ===");
            while (true) {
                if (currentUser == null) showAuthMenu();
                else showMainMenu();
            }
        }

        private static void showAuthMenu() {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choice: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Username: "); String u = sc.nextLine();
                users.put(u, new User(u));
                System.out.println("Registered. Please login.");
            } else if (choice.equals("2")) {
                System.out.print("Username: "); String u = sc.nextLine();
                if (users.containsKey(u)) {
                    currentUser = users.get(u);
                    System.out.println("Welcome, " + u + "!");
                } else {
                    System.out.println("User not found.");
                }
            } else System.exit(0);
        }

        private static void showMainMenu() {
            System.out.println("\nLogged in as: " + currentUser.getUsername());
            System.out.println("1. Create Group\n2. Join Group (Code)\n3. Logout");
            System.out.print("Choice: "); String choice = sc.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Group Code: "); String code = sc.nextLine();
                    groups.put(code, new Group(code, currentUser));
                    System.out.println("Group created!");
                }
                case "2" -> {
                    System.out.print("Group Code: "); String code = sc.nextLine();
                    if (groups.containsKey(code)) {
                        groups.get(code).addPending(currentUser);
                        System.out.println("Request sent.");
                    } else {
                        System.out.println("Group not found.");
                    }
                }
                case "3" -> currentUser = null;
                default -> System.out.println("Invalid choice.");
            }
        }

    }
