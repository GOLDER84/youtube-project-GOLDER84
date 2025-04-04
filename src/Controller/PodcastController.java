package Controller;

import Model.*;

public class PodcastController extends ContentController{
    private Podcast podcast;
    private DatabaseController databaseController;
    public static PodcastController instance;
    public PodcastController() {
        this.databaseController = DatabaseController.getInstance();
    }
    public static PodcastController getInstance() {
        if (instance == null) {
            instance = new PodcastController();
        }
        return instance;
    }

    @Override
    public String addContent(int ownerId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitle, boolean isExclusive, VideoType type) {
        return "";
    }

    @Override
    public String playContent(User currentUser) {
        if (podcast.isExclusive() & !(currentUser instanceof PremiumUser)){
            return "Podcast is exclusive and cannot be played";
        }
        podcast.setViews(podcast.getViews() + 1);
        return String.format("Now playing: %s\nDuration: %d seconds\nCategory: %s", podcast.getName(), podcast.getDuration(), podcast.getCategory());
    }

    @Override
    public String removeContent(int podcastId, int requestingUserId) {
        Content content = databaseController.getContentById(podcastId);
        if (!(content instanceof Podcast)) {
            return "Podcast not found";
        }
        Podcast podcast = (Podcast) content;
        User requester = databaseController.getUserById(requestingUserId);
        if (requester == null) {
            return "User not found";
        }
        boolean isOwner = (podcast.getOwnerOfPodcast().getId() == requestingUserId);
        boolean isAdmin = (requester.getId() == Admin.getInstance().getId());
        if (!isOwner && !isAdmin) {
            return "You do not have permission to delete this podcast";
        }
        databaseController.removeContent(podcast);
        return "Podcast removed";
    }
}
