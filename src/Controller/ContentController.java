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
    private DatabaseController databaseController = DatabaseController.getInstance();
    private ChannelController channelController = ChannelController.getInstance();
    private UserController userController = UserController.getInstance();
    private Channel channel;
    private static ContentController instance;
    private User lastSignUpUser;

    public static ContentController getInstance() {
        if (instance == null) {
            instance = new ContentController();
        }
        return instance;
    }
//    public String showContent(int contentId) {
//        Content content = databaseController.getContentById(contentId);
//        if (content == null) {
//            return "Content not found";
//        }
//        if (content instanceof NormalVideo){
//            return String.format("Streaming NormalVideo: %s\nName: %s\nDescription: %s\nViews: %s\nLikes %s\nCategory: %s\nTime:" , content.getName() ,content.getDescription() , content.getViews() , content.getLikes() ,  content.getCategory() , content.getDuration());
//        }
//        if (content instanceof ShortVideo){
//            return String.format("Streaming ShortVideo: %s\nName: %s\nDescription: %s\nViews: %s\nLikes %s\nCategory: %s\nTime:" , content.getName() ,content.getDescription() , content.getViews() , content.getLikes() ,  content.getCategory() , content.getDuration());
//        }
//        if (content instanceof LiveStream){
//            return String.format("Streaming LiveStream: %s\nName: %s\nDescription: %s\nViews: %s\nLikes %s\nCategory: %s\nTime: %s\nSubtitle: " , content.getName() ,content.getDescription() , ((LiveStream) content).getCurrentViewers() , content.getLikes() ,  content.getCategory() , content.getDuration() , ((LiveStream) content).getSubtitles());
//        }
//        return String.format("Streaming Podcast: %s\nName: %s\nDescription: %s\nViews: %s\nLikes %s\nCategory: %s\nTime:" , content.getName() ,content.getDescription() , content.getViews() , content.getLikes() ,  content.getCategory() , content.getDuration());
//    }
    public ArrayList<Content> sortContents(String sortType) {
        ArrayList<Content> contents = new ArrayList<>(databaseController.getContents());

        switch (sortType.toUpperCase()) {
            case "L" -> contents.sort(Comparator.comparingInt(Content::getLikes).reversed());
            case "V" -> contents.sort(Comparator.comparingInt(Content::getViews).reversed());
            default -> throw new IllegalArgumentException("Invalid sort type. Use 'L' or 'V'");
        }
        return contents;
    }

    public ArrayList<Content> filterByType(String contentType) {
        ArrayList<Content> contents = new ArrayList<>();
        if(contentType.equals("V")){
            for(Content c : databaseController.getContents()){
                if (c instanceof Video){
                    contents.add(c);
                }
            }
        }
        else if(contentType.equals("P")){
            for(Content c : databaseController.getContents()){
                if (c instanceof Podcast){
                    contents.add(c);
                }
            }
        }
        return contents;
    }

    public ArrayList<Content> filterAdvanced(String filterType, String filterValue) {
        ArrayList<Content> allContents = databaseController.getContents();
        ArrayList<Content> filteredContents = new ArrayList<>();
        char upperFilterType = filterType.toUpperCase().charAt(0);
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

//    public ArrayList<Content> showOfferedContent(String type) {
//        ArrayList<Content> allContent = databaseController.getContents();
//        ArrayList<Content> filteredContent = new ArrayList<>();
//
//        for (Content content : allContent) {
//            if (("video".equalsIgnoreCase(type) && content instanceof NormalVideo) ||
//                    ("podcast".equalsIgnoreCase(type) && content instanceof Podcast) ||
//                    ("shortvideo".equalsIgnoreCase(type) && content instanceof ShortVideo) ||
//                    ("livestream".equalsIgnoreCase(type) && content instanceof LiveStream)) {
//                filteredContent.add(content);
//            }
//        }
//        return filteredContent;
//    }
    public String createPodcast( String name,String description,
                                 String duration, String category,
                                 String fileLink, String cover , String isExclusive){
        Content podcast = new Podcast(userController.lastSignedUpUser.getId() , userController.getUserChannelId() , name , description , duration, Category.valueOf(category.toUpperCase()), fileLink, cover , isExclusive.equals("Y"));
        databaseController.addContent(podcast);
        channelController.addContentToChannel(podcast.getId());
        return "Podcast created";
    }
    public String createLiveStream(String name ,String isExclusive, String description, String duration, String category, String fileLink, String cover, String subtitles , String scheduledTime){
        Content liveStream = new LiveStream(userController.lastSignedUpUser.getId() , userController.getUserChannelId(), name ,isExclusive.equals("Y"), description , duration , Category.valueOf(category.toUpperCase()) , fileLink , cover , subtitles , 0 , LocalDate.parse(scheduledTime));
        databaseController.addContent(liveStream);
        channelController.addContentToChannel(liveStream.getId());
        return "Live stream created";
    }

    public String createNormalVideo(String name , String isExclusive, String description, String duration, String category, String fileLink, String cover, String subtitles ,String quality ,String format){
        Content normalVideo = new NormalVideo(userController.lastSignedUpUser.getId() , userController.getUserChannelId(), name ,isExclusive.equals("Y") ,description , duration , Category.valueOf(category.toUpperCase()) , fileLink , cover , subtitles , Format.valueOf(format) , Quality.valueOf(quality));
        databaseController.addContent(normalVideo);
        channelController.addContentToChannel(normalVideo.getId());
        return "Normal video created";
    }

    public String createShortVideo(String name , String isExclusive, String description, String duration, String category, String fileLink, String cover, String subtitles , String referenceMusic){
        Content shortVideo = new ShortVideo(userController.lastSignedUpUser.getId() , userController.getUserChannelId() , name ,isExclusive.equals("Y") ,  description , duration , Category.valueOf(category.toUpperCase()) , fileLink , cover , subtitles , referenceMusic);
        databaseController.addContent(shortVideo);
        channelController.addContentToChannel(shortVideo.getId());
        return "Short video created";
    }

//    public abstract String addContent(int ownerId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitle, boolean isExclusive, VideoType type);
//    public abstract String playContent(User currentUser);
//    public abstract String removeContent(int podcastId, int requestingUserId);
}
