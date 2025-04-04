package Controller;

import Model.Channel;
import Model.Database;
import Model.*;

import java.time.LocalDate;

public class ChannelController {
    private Channel channel;
    private DatabaseController databaseController;
    private ContentController contentController;
    private static ChannelController instance;

    public ChannelController() {
        this.databaseController = DatabaseController.getInstance();
        this.contentController = ContentController.getInstance();
    }
    public static ChannelController getInstance() {
        if (instance == null) {
            instance = new ChannelController();
        }
        return instance;
    }
    public String showSubscribersList() {
        StringBuilder result = new StringBuilder("Subscribers:\n");
        for (int subscriberId : channel.getSubscribersList()) {
            User subscriber = databaseController.getUserById(subscriberId);
            if (subscriber != null) {
                result.append("- ").append(subscriber.getUsername()).append("\n");
            }
        }
        return result.toString();
    }

    public String viewChannel() {
        return String.format(
                "Channel Info:\nName: %s\nDescription: %s\nSubscribers: %d\n", channel.getChannelName(), channel.getChannelDescription(), channel.getSubscribersList().size()
        );
    }

    public String editChannel(String name, String description, String cover) {
        channel.setChannelName(name);
        channel.setChannelDescription(description);
        channel.setChannelCover(cover);
        return "Channel updated successfully";
    }

    public String addSubscriber(int userId) {
        User user = databaseController.getUserById(userId);
        if (user == null) {
            return "User not found";
        }
        channel.getSubscribersList().add(userId);
        return "Subscriber added";
    }
    public String addPodcastToChannel(int ownerId, int channelId, boolean isExclusive , String name, String description, int duration, Category category, String fileLink, String cover ,User ownerOfPodcast) {
        Channel channel = databaseController.getChannelById(channelId);
        if (channel == null) {
            return "Channel not found";
        }
        if (channel.getChannelOwnerId() != ownerId) {
            return "You don't have permission to add content to this channel";
        }
        if (duration <= 0){
            return "Invalid duration";
        }
        String result = contentController.createPodcast(ownerId , channelId , description , name , duration , category ,fileLink, cover , ownerOfPodcast , isExclusive);
        Playlist allContent = channel.getPlaylists().stream()
                .filter(p -> p.getPlaylistName().equals("All Content"))
                .findFirst()
                .orElseGet(() -> {
                    Playlist newPlaylist = new Playlist("All Content");
                    channel.getPlaylists().add(newPlaylist);
                    return newPlaylist;});
        Content newPodcast = databaseController.getContents().stream()
                .filter(c -> c.getName().equals(name) && c.getOwnerId() == ownerId)
                .findFirst()
                .orElse(null);

        if (newPodcast != null) {
            allContent.getContentList().add(newPodcast);
            return "Podcast added to channel successfully";
        } else {
            return "Failed to add podcast to channel";
        }
    }
    String addNormalVideoToChannel(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitles , Format format, Quality quality) {
        Channel channel = databaseController.getChannelById(channelId);
        if (channel == null) {
            return "Channel not found";
        }
        if (channel.getChannelOwnerId() != ownerId) {
            return "You don't have permission to add content to this channel";
        }
        if (duration <= 0){
            return "Invalid duration";
        }
        String result = contentController.createNormalVideo(ownerId , channelId , name , description , duration , category , fileLink , cover , subtitles , format , quality);
        Playlist allContent = channel.getPlaylists().stream()
                .filter(p -> p.getPlaylistName().equals("All Content"))
                .findFirst()
                .orElseGet(() -> {
                    Playlist newPlaylist = new Playlist("All Content");
                    channel.getPlaylists().add(newPlaylist);
                    return newPlaylist;});
        Content newNormalVideo = databaseController.getContents().stream().filter(c -> c.getName().equals(name) && c.getOwnerId() == ownerId).findFirst().orElse(null);
        if (newNormalVideo != null) {
            allContent.getContentList().add(newNormalVideo);
            return "Video added to channel successfully";
        }else {
            return "Failed to add normal video to channel";
        }
    }
    String addShortVideoToChannel(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitles , String referenceMusic){
        Channel channel = databaseController.getChannelById(channelId);
        if (channel == null) {
            return "Channel not found";
        }
        if (channel.getChannelOwnerId() != ownerId) {
            return "You don't have permission to add content to this channel";
        }
        if (duration <= 0 || duration > 30){
            return "Short video duration must be between 1 and 30 seconds";
        }
        String result = contentController.createShortVideo(ownerId , channelId , name , description , duration , category , fileLink , cover , subtitles , referenceMusic);
        Playlist allContent = channel.getPlaylists().stream()
                .filter(p -> p.getPlaylistName().equals("All Content"))
                .findFirst()
                .orElseGet(() -> {
                    Playlist newPlaylist = new Playlist("All Content");
                    channel.getPlaylists().add(newPlaylist);
                    return newPlaylist;});
        Content newShortVideo = databaseController.getContents().stream().filter(c -> c.getName().equals(name) && c.getOwnerId() == ownerId).findFirst().orElse(null);
        if (newShortVideo != null) {
            allContent.getContentList().add(newShortVideo);
            return "Video added to channel successfully";
        }else {
            return "Failed to add short video to channel";
        }
    }
    String addLiveStreamToChannel(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitles , int currentViewers, LocalDate scheduledTime){
        Channel channel = databaseController.getChannelById(channelId);
        if (channel == null) {
        return "Channel not found";
        }
        if (channel.getChannelOwnerId() != ownerId) {
        return "You don't have permission to add content to this channel";
        }
        if (scheduledTime.isBefore(LocalDate.now())) {
        return "Scheduled time must be in the future";
        }
        String result = contentController.createLiveStream(ownerId , channelId , name , description , duration , category , fileLink , cover , subtitles , 0, scheduledTime);
        Playlist allContent = channel.getPlaylists().stream()
                .filter(p -> p.getPlaylistName().equals("All Content"))
                .findFirst()
                .orElseGet(() -> {
                    Playlist newPlaylist = new Playlist("All Content");
                    channel.getPlaylists().add(newPlaylist);
                    return newPlaylist;});
        Content newLiveStream = databaseController.getContents().stream().filter(s -> s.getName().equals(name) &&s.getOwnerId() == ownerId).findFirst().orElse(null);
        if (newLiveStream != null) {
            allContent.getContentList().add(newLiveStream);
            return "Live stream added to channel successfully";
        }
        return "Failed to add live stream to channel";
    }
    String createChannelPlaylist(String playlistName){
        Playlist newPlaylist = new Playlist(playlistName);
        channel.getPlaylists().add(newPlaylist);
        if (channel.getPlaylists().size() == 1) {
            Playlist allContent = new Playlist("All Content");
            channel.getPlaylists().add(allContent);
        }
        databaseController.updateChannel(channel);
        return "Playlist '" + playlistName + "' created successfully in channel";
    }
}
