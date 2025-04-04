package Model;

public class NormalVideo extends Video {

    private Format format;
    private Quality quality;

    public NormalVideo(int ownerId, int channelId, String name, String description, int duration, Category category, String fileLink, String cover, String subtitles , Format format, Quality quality) {
        super(ownerId, channelId, name, description, duration, category, fileLink, cover, subtitles);
        this.format = format;
        this.quality = quality;
    }
    public Format getFormat() {
        return format;
    }
    public void setFormat(Format format) {
        this.format = format;
    }
    public Quality getQuality() {
        return quality;
    }
    public void setQuality(Quality quality) {
        this.quality = quality;
    }
}
