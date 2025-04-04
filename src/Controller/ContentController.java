package Controller;
import Model.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public class ContentController {
    private Content content;
    private DatabaseController databaseController;
    private static ContentController instance;
    public ContentController() {
        this.databaseController = DatabaseController.getInstance();
    }

    public static ContentController getInstance() {
        if (instance == null) {
            instance = new ContentController();
        }
        return instance;
    }

    public String showContent(int contentId) {
        Content content = databaseController.getContentById(contentId);
        if (content == null) {
            return "Content not found";
        }
        if (content instanceof NormalVideo){
            return String.format("Streaming NormalVideo: %s\nName: %s\nDescription: %s\nViews: %s\nLikes %s\nCategory: %s\nTime:" , content.getName() ,content.getDescription() , content.getViews() , content.getLikes() ,  content.getCategory() , content.getDuration());
        }
        if (content instanceof ShortVideo){
            return String.format("Streaming ShortVideo: %s\nName: %s\nDescription: %s\nViews: %s\nLikes %s\nCategory: %s\nTime:" , content.getName() ,content.getDescription() , content.getViews() , content.getLikes() ,  content.getCategory() , content.getDuration());
        }
        if (content instanceof LiveStream){
            return String.format("Streaming LiveStream: %s\nName: %s\nDescription: %s\nViews: %s\nLikes %s\nCategory: %s\nTime: %s\nSubtitle: " , content.getName() ,content.getDescription() , ((LiveStream) content).getCurrentViewers() , content.getLikes() ,  content.getCategory() , content.getDuration() , ((LiveStream) content).getSubtitles());
        }
        return String.format("Streaming Podcast: %s\nName: %s\nDescription: %s\nViews: %s\nLikes %s\nCategory: %s\nTime:" , content.getName() ,content.getDescription() , content.getViews() , content.getLikes() ,  content.getCategory() , content.getDuration());
    }
    public ArrayList<Content> sortContents(char sortType) {
        ArrayList<Content> contents = new ArrayList<>(databaseController.getContents());

        switch (Character.toUpperCase(sortType)) {
            case 'L' -> contents.sort(Comparator.comparingInt(Content::getLikes).reversed());
            case 'V' -> contents.sort(Comparator.comparingInt(Content::getViews).reversed());
            default -> throw new IllegalArgumentException("Invalid sort type. Use 'L' or 'V'");
        }
        return contents;
    }

    public ArrayList<Content> filterByType(char contentType) {
        return databaseController.getContents().stream()
                .filter(content -> {
                    switch (Character.toUpperCase(contentType)) {
                        case 'V' -> { return content instanceof Video; }
                        case 'P' -> { return content instanceof Podcast; }
                        default -> throw new IllegalArgumentException("Invalid content type. Use 'V' or 'P'");
                    }}).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Content> filterAdvanced(char filterType, String filterValue) {
        ArrayList<Content> allContents = databaseController.getContents();
        ArrayList<Content> filteredContents = new ArrayList<>();
        char upperFilterType = Character.toUpperCase(filterType);
        String upperFilterValue = filterValue.toUpperCase();

        for (Content content : allContents) {
            if (matchesFilter(content, upperFilterType, upperFilterValue)) {
                filteredContents.add(content);
            }
        }
        return filteredContents;
    }

    public boolean matchesFilter(Content content, char filterType, String filterValue) {
        switch (filterType) {
            case 'C':
                return content.getCategory().name().equalsIgnoreCase(filterValue);

            case 'D':
                return matchesDateFilter(content.getPublishDate(), filterValue);

            default:
                throw new IllegalArgumentException("Invalid filter type. Use 'C' or 'D'");
        }
    }

    private boolean matchesDateFilter(Date contentDate, String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String contentDateStr = sdf.format(contentDate);
            return contentDateStr.equals(dateString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Use 'yyyy-MM-dd' format");
        }
    }

    public ArrayList<Content> showOfferedContent(String type) {
        ArrayList<Content> allContent = databaseController.getContents();
        ArrayList<Content> filteredContent = new ArrayList<>();

        for (Content content : allContent) {
            if (("video".equalsIgnoreCase(type) && content instanceof NormalVideo) ||
                    ("podcast".equalsIgnoreCase(type) && content instanceof Podcast) ||
                    ("shortvideo".equalsIgnoreCase(type) && content instanceof ShortVideo) ||
                    ("livestream".equalsIgnoreCase(type) && content instanceof LiveStream)) {
                filteredContent.add(content);
            }
        }
        return filteredContent;
    }
    public String createPodcast(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover ,User ownerOfPodcast , boolean isExclusive){
        Content podcast = new Podcast(ownerId , channelId , name , description , duration, category, fileLink, cover , ownerOfPodcast , isExclusive);
        databaseController.addContent(podcast);
        return "Podcast created";
    }
    public String createLiveStream(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitles , int currentViewers, LocalDate scheduledTime){
        Content liveStream = new LiveStream(ownerId , channelId , name , description , duration , category , fileLink , cover , subtitles , currentViewers , scheduledTime);
        databaseController.addContent(liveStream);
        return "Live stream created";
    }

    public String createNormalVideo(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitles , Format format, Quality quality){
        Content normalVideo = new NormalVideo(ownerId , channelId , name , description , duration , category , fileLink , cover , subtitles , format , quality);
        databaseController.addContent(normalVideo);
        return "Normal video created";
    }

    public String createShortVideo(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitles , String referenceMusic){
        Content shortVideo = new ShortVideo(ownerId , channelId , name , description , duration , category , fileLink , cover , subtitles , referenceMusic);
        databaseController.addContent(shortVideo);
        return "Short video created";
    }

//    public abstract String addContent(int ownerId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitle, boolean isExclusive, VideoType type);
//    public abstract String playContent(User currentUser);
//    public abstract String removeContent(int podcastId, int requestingUserId);
}
