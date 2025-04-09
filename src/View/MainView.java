package View;

import java.util.ArrayList;
import java.util.Scanner;

import Controller.*;
import Model.Category;
import Model.Channel;
import Model.Content;
import Model.PremiumSubscriptionPackages;

public class MainView {
    Scanner input = new Scanner(System.in);
    private UserController userController = UserController.getInstance();
    private ReportController reportController = ReportController.getInstance();
    private PodcastController podcastController = PodcastController.getInstance();
    private PlaylistController playlistController = PlaylistController.getInstance();
    private DatabaseController databaseController = DatabaseController.getInstance();
    private ContentController contentController = ContentController.getInstance();
    private CommentController commentController = CommentController.getInstance();
    private ChannelController channelController = ChannelController.getInstance();
    private AdminController adminController = AdminController.getInstance();

    public void start() {
        while (true) {
            String command = input.nextLine();
            String[] commands = command.split(" - ");
            String commandType = commands[0];
            if (commandType.equals("Signup")) {
                System.out.println(userController.createAccount(commands[1], commands[2], commands[3] + commands[4], commands[5], commands[6], commands[7]));
            } else if (commandType.equals("Login")) {
                String result = userController.login(commands[1], commands[2]);
                if (!(result.contains("Welcome"))) {
                    System.out.println(adminController.login(commands[1], commands[2]));
                } else {
                    System.out.println(result);
                }
            } else if (commandType.equals("Logout")) {
                String result = userController.logout();
                if (result.equals("You are not logged in")) {
                    System.out.println(adminController.logout());
                }
                else {
                    System.out.println(result);
                }
            } else if (commandType.equals("FavouriteCategories")) {
                System.out.println(userController.chooseFavouriteCategory(handelFavouriteCategories(commands)));
            } else if (commandType.equals("AccountInfo")) {
                if (userController.showUserInfo().contains("User")) {
                    System.out.println(userController.showUserInfo());
                } else if (adminController.adminInfo().contains("Admin")) {
                    System.out.println(adminController.adminInfo());
                }
            } else if (commandType.equals("EditUserInfo")) {
                if (commands[1].equals("N")) {
                    System.out.println(userController.editProfile(commands[2]));
                } else if (commands[1].equals("P")) {
                    System.out.println(userController.editPassword(commands[2]));
                }
            } else if (commandType.equals("CreatePlaylist")) {
                if (commands[1].equals("U")) {
                    System.out.println(userController.createPlaylist(commands[2]));
                } else if (commands[1].equals("C")) {
                    System.out.println(channelController.createChannelPlaylist(commands[2]));
                }
            } else if (commandType.equals("AddToPlaylist")) {
                System.out.println(userController.addContentToPlaylist(Integer.parseInt(commands[1]), Integer.parseInt(commands[2])));
            } else if (commandType.equals("Play")) {
                System.out.println(userController.playContent(Integer.parseInt(commands[1])));
            } else if (commandType.equals("Like")) {
                System.out.println(userController.likeContent(Integer.parseInt(commands[1])));
            } else if (commandType.equals("Report")) {
                System.out.println(userController.reportContent(Integer.parseInt(commands[1]), commands[2]));
            } else if (commandType.equals("Search")) {
                System.out.println(userController.searchContent(commands[1]));
            } else if (commandType.equals("Subscribe")) {
                System.out.println(userController.subscribeChannel(Integer.parseInt(commands[1])));
            } else if (commandType.equals("ShowPlaylists")) {
                System.out.println(userController.showPlaylistContents());
            } else if (commandType.equals("GetSuggestions")) {
                System.out.println(userController.showSuggestedContents());
            } else if (commandType.equals("ShowSubscriptions")) {
                for (Channel subscription : userController.showSubscribedChannels()) {
                    System.out.println(subscription.getChannelName());
                }
            } else if (commandType.equals("ShowChannels")) {
                for (Channel c : userController.showChannels()) {
                    System.out.println(c.getChannelName());
                }
            } else if (commandType.equals("ShowChannel")) {
                System.out.println(userController.showChannelInfoAndContents(Integer.parseInt(commands[1])));
            } else if (commandType.equals("AddComment")) {
                System.out.println(userController.addComment(Integer.parseInt(commands[1]), commands[2]));
            } else if (commandType.equals("GetPremium")) {
                System.out.println(userController.buySubscription(PremiumSubscriptionPackages.valueOf(commands[1].toUpperCase())));
            } else if (commandType.equals("Sort")) {
                for (Content c : contentController.sortContents(commands[1])) {
                    System.out.println(c.getName());
                }
            } else if (commandType.equals("Filter")) {
                if (commands[1].equals("V") || commands[1].equals("P")) {
                    for (Content c : contentController.filterByType(commands[1])) {
                        System.out.println(c.getName());
                    }
                } else if (commands[1].equals("C") || commands[1].equals("D")) {
                    for (Content c : contentController.filterAdvanced(commands[1], commands[2])) {
                        System.out.println(c.getName());
                    }
                }
            } else if (commandType.equals("IncreaseCredit")) {
                System.out.println(userController.addBalance(Integer.parseInt(commands[1])));
            } else if (commandType.equals("CreateChannel")) {
                System.out.println(userController.createChannel(commands[1], commands[2], commands[3]));
            } else if (commandType.equals("ViewChannel")) {
                System.out.println(channelController.viewChannel());
            } else if (commandType.equals("Publish")) {
                if (commands[1].equals("P")) {
                    System.out.println(contentController.createPodcast(commands[3], commands[4], commands[5], commands[6], commands[7], commands[8], commands[2]));
                } else if (commands[1].equals("NV")) {
                    System.out.println(contentController.createNormalVideo(commands[3], commands[2], commands[4], commands[5], commands[6], commands[7], commands[8], commands[9], commands[10], commands[11]));
                } else if (commands[1].equals("SV")) {
                    System.out.println(contentController.createShortVideo(commands[3], commands[2], commands[4], commands[5], commands[6], commands[7], commands[8], commands[9], commands[10]));
                } else if (commands[1].equals("LS")) {
                    System.out.println(contentController.createLiveStream(commands[3], commands[2], commands[4], commands[5], commands[6], commands[7], commands[8], commands[9], commands[10]));
                }
            } else if (commandType.equals("ShowChannelContent")) {
                System.out.println(userController.showChannelContentAndInfo());
            } else if (commandType.equals("ShowChannelSubscribers")) {
                System.out.println(userController.showChannelSubscribers());
            } else if (commandType.equals("Edit")) {
                if (commands[1].equals("N")) {
                    System.out.println(userController.editProfile(commands[2]));
                } else if (commands[1].equals("D")) {
                    System.out.println(userController.editPassword(commands[2]));
                }
            } else if (commandType.equals("ViewPopularChannels")) {
                System.out.println(adminController.showPopularChannelOnSubscribers());
            } else if (commandType.equals("ViewPopularContents")) {
                System.out.println(adminController.showPopularContentOnLikes());
            } else if (commandType.equals("Users")) {
                System.out.println(adminController.showAllUserAccountInfo());
            } else if (commandType.equals("Contents")) {
                System.out.println(adminController.showAllContentInfo());
            } else if (commandType.equals("Reports")) {
                System.out.println(adminController.showAllReport());
            } else if (commandType.equals("ManageReport")) {
                if (commands[2].equals("C")) {
                    System.out.println(adminController.acceptReport(Integer.parseInt(commands[1])));
                } else {
                    System.out.println("Report not accepted");
                }
            } else if (commandType.equals("UnbanUser")) {
                System.out.println(adminController.unbanUser(Integer.parseInt(commands[1])));
            } else if (commandType.equals("Exit")) break;
        }
        System.out.println("The End");
    }

    public ArrayList<Category> handelFavouriteCategories(String[] commands) {
        String[] categories = commands[1].split(", ");
        ArrayList<Category> category1 = new ArrayList<>();
        for (String category : categories) {
            category1.add(Category.valueOf(category.toUpperCase()));
        }
        return category1;
    }
}
