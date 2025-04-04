package Controller;

import Model.*;
import java.util.ArrayList;


public class AdminController {
    private Admin admin;
    private Database database;
    private DatabaseController databaseController;
    private static AdminController instance;
    public AdminController() {
        this.admin = Admin.getInstance();
        this.database = Database.getInstance();
        this.databaseController = DatabaseController.getInstance();
    }
    public static AdminController getInstance() {
        if (instance == null) {
            instance = new AdminController();
        }
        return instance;
    }

    public String login(String username, String password) {
        if (this.admin != null) {
            return "An admin account is already logged in.";
        }
        Admin admin = Admin.getInstance();
        if (!(admin.getUsername().equals(username))){
            return "Invalid username";
        }
        if (!(admin.getPassword().equals(password))){
            return "Invalid password";
        }
        this.admin = admin;
        return "Admin logged in.";
    }

    public String logout() {
        if (this.admin == null) {
            return "You are not logged in";
        }
        this.admin = null;
        return "Logged out successfully";
    }
    public String adminInfo(){
        if (this.admin == null) {
            return "You are not logged in";
        }
        String info = String.format(
                "=== Admin Account Information ===\n" +
                        "ID: %d\n" +
                        "Username: %s\n" +
                        "Full Name: %s\n" +
                        "Email: %s\n" +
                        "Phone: %s\n" +
                        "Profile Cover: %s\n" +
                        "===============================",
                admin.getId(),
                admin.getUsername(),
                admin.getFullName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getProfileCover()
        );
        return info;
    }
    public String createAdmin(String username, String password , String name , String email , String phone , String profileCover) {
        String validation = checkSignupInputs(username, password, name, email, phone, profileCover);
        if (validation != null) {
            return validation;
        }
        return "Error creating new admin";
    }

    public String checkSignupInputs(String userName, String password, String name, String email, String phoneNumber, String profileCover) {
        if (userName == null || userName.isEmpty()) return "Username cannot be empty";
        if (password == null || password.isEmpty()) return "Password cannot be empty";
        if (!checkPasswordStrength(password)) return "Password is too weak";
        if (name == null || name.isEmpty()) return "Name cannot be empty";
        return "Valid";
    }

    public boolean checkPasswordStrength(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*");
    }

    public String showPopularChannelOnSubscribers() {
        ArrayList<Channel> channels = database.getAllChannel();
        if (channels.isEmpty()) return "No channels available";

        channels.sort((c1, c2) -> Integer.compare(c2.getSubscribersList().size(), c1.getSubscribersList().size()));

        StringBuilder result = new StringBuilder("Most popular channels:\n");
        for (int i = 0; i < Math.min(5, channels.size()); i++) {
            result.append(i+1).append(". ").append(channels.get(i).getChannelName()).append(" (").append(channels.get(i).getSubscribersList().size()).append(" subscribers)\n");
        }
        return result.toString();
    }

    public String showPopularContentOnLikes() {
        ArrayList<Content> contents = database.getAllContent();
        if (contents.isEmpty()) return "No content available";

        contents.sort((c1, c2) -> Integer.compare(c2.getLikes(), c1.getLikes()));

        StringBuilder result = new StringBuilder("Most popular content:\n");
        for (int i = 0; i < Math.min(5, contents.size()); i++) {
            result.append(i+1).append(". ").append(contents.get(i).getName()).append(" (").append(contents.get(i).getLikes()).append(" likes)\n");
        }
        return result.toString();
    }

    public String showUserAccountInfo(int userId) {
        User user = databaseController.getUserById(userId);
        if (user == null) return "User not found";

        return String.format("User Info:\nID: %d\nUsername: %s\nName: %s\nEmail: %s\nPhone: %s\nType: %s", user.getId(), user.getUsername(), user.getUsername(), user.getEmail(), user.getPhone(), (user instanceof PremiumUser) ? "Premium" : "Normal");
    }

    public String showAllUserAccountInfo() {
        ArrayList<User> users = databaseController.getUsers();
        if (users.isEmpty()) return "No users available";

        StringBuilder result = new StringBuilder("All Users:\n");
        for (User user : users) {
            result.append(String.format("ID: %d, Username: %s, Name: %s, Type: %s\n", user.getId(), user.getUsername(), user.getUsername(), (user instanceof PremiumUser) ? "Premium" : "Normal"));
        }
        return result.toString();
    }

    public String showChannelAndContent(int channelId) {
        Channel channel = databaseController.getChannelById(channelId);
        if (channel == null) return "Channel not found";

        StringBuilder result = new StringBuilder(String.format("Channel Info:\nName: %s\nDescription: %s\nSubscribers: %d\n", channel.getChannelName(), channel.getChannelDescription(), channel.getSubscribersList().size()));

        result.append("Contents:\n");
        for (int contentId : channel.getContentId()) {
            Content content = databaseController.getContentById(contentId);
            if (content != null) {
                result.append("- ").append(content.getName()).append("\n");
            }
        }
        return result.toString();
    }

    public String showAllChannelsAndContents() {
        ArrayList<Channel> channels = databaseController.getChannels();
        if (channels.isEmpty()) return "No channels available";

        StringBuilder result = new StringBuilder("All Channels:\n");
        for (Channel channel : channels) {
            result.append(String.format("Channel: %s (ID: %d)\nContents:\n", channel.getChannelName(), channel.getChannelId()));

            for (int contentId : channel.getContentId()) {
                Content content = databaseController.getContentById(contentId);
                if (content != null) {
                    result.append("- ").append(content.getName()).append("\n");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    public String acceptReport(int reportId) {
        Report report = databaseController.getReports().get(reportId);
        if (report == null) return "Report not found";
        Content content = databaseController.getContentById(report.getContentId());
        if (content == null) return "Content not found";
        databaseController.getReports().remove(reportId);
        databaseController.removeContent(content);
        banUser(report.getReportedUserId());
        return "Report accepted and content removed and user banned";
    }

    public void banUser(int userId) {
        User user = databaseController.getUserById(userId);
        if (user == null) return;
        database.getAllBanUser().add(user);
    }

    public String unbanUser(int userId) {
        ArrayList<User> bannedUsers = database.getAllBanUser();
        User user = bannedUsers.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);

        if (user == null) return "User not found in banned list";
        bannedUsers.remove(user);
        return "User unbanned successfully";
    }

}
