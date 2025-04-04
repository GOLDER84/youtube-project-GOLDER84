package Controller;

import Model.*;
import java.util.*;

public class VideoController extends ContentController {
    private Video video;
    private DatabaseController databaseController;
    private static VideoController instance;
    public VideoController() {
        this.databaseController = DatabaseController.getInstance();
    }
    public static VideoController getInstance() {
        if (instance == null) {
            instance = new VideoController();
        }
        return instance;
    }

    @Override
    public String addContent(int ownerId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitle, boolean isExclusive, VideoType type) {
        if (name == null || name.trim().isEmpty()) {
            return "Video name cannot be empty!";
        }
        if (duration <= 0) {
            return "Duration must be positive!";
        }
        if (type == VideoType.SHORT && duration > 30) {
            return "Short videos must be under 30 seconds!";
        }

        return "Video added successfully! ID: " + video.getId();
//        if (video.getName() == null || video.getName().trim().isEmpty()) {
//            return "Video name cannot be empty";
//        }
//        if (video.getDuration() <= 0){
//            return "Video duration cannot be less than 0";
//        }
//        if (video instanceof ShortVideo && video.getDuration() > 30){
//            return "Short videos must be less than 30 seconds";
//        }
//        if (video instanceof NormalVideo){
//            ((NormalVideo) video).setDuration(video.getDuration());
//        }
    }

    @Override
    public String playContent(User currentUser) {

    }


    @Override
    public String removeContent(int videoId , int requesterUserId) {

    }
}
