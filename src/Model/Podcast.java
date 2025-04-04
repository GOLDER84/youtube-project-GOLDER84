package Model;

public class Podcast extends Content{
    private User ownerOfPodcast;
    private boolean isExclusive;

    public Podcast(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover ,User ownerOfPodcast , boolean isExclusive) {
        super(ownerId, channelId, name, description, duration, category, fileLink, cover);
        this.ownerOfPodcast = ownerOfPodcast;
        this.isExclusive = isExclusive;
    }
    public User getOwnerOfPodcast() {
        return ownerOfPodcast;
    }
    public void setOwnerOfPodcast(User ownerOfPodcast) {
        this.ownerOfPodcast = ownerOfPodcast;
    }
    public boolean isExclusive() {
        return isExclusive;
    }
    public void setExclusive(boolean exclusive) {
        isExclusive = exclusive;
    }
}
