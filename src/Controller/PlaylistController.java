package Controller;

import Model.*;

public class PlaylistController {
    private Playlist playlist;
    private DatabaseController databaseController = DatabaseController.getInstance();
    private static PlaylistController instance;
    public PlaylistController() {
    }
    public static PlaylistController getInstance() {
        if (instance == null) {
            instance = new PlaylistController();
        }
        return instance;
    }
    public Playlist createPlaylist(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Playlist newPlaylist = new Playlist(name);
        playlist = newPlaylist;
        return newPlaylist;
    }

    public Playlist getPlaylistById(int userId, int playlistId) {
        User user = databaseController.getUserById(userId);
        if (user == null) return null;

        return user.getPlaylists().stream().filter(p -> p.getId() == playlistId).findFirst().orElse(null);
    }


    public String addContentToPlaylist(int contentId) {
        Content content = databaseController.getContentById(contentId);
        if (content == null){
            return "Content not found";
        }
        if (playlist.getContentList().contains(content)) {
            return "Content already exists";
        }
        playlist.getContentList().add(content);
        return "Content added to playlist Successfully";
    }

    public String removeContentFromPlaylist(int contentId) {
        Content content = databaseController.getContentById(contentId);
        if (content == null) return "Content not found";
        if (!playlist.getContentList().contains(content)) {
            return "Content does not exist";
        }
        playlist.getContentList().remove(content);
        return "Content removed from playlist Successfully";
    }

    public String renamePlaylist(int playlistId, String newName) {
        if (newName == null || newName.isEmpty()) {
            return "Renaming playlist failed";
        }
        playlist.setPlaylistName(newName);
        return "Renaming playlist Successfully";
    }
}
