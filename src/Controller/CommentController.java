package Controller;
import Model.*;

import java.time.LocalDate;
import java.util.Date;

public class CommentController {
    private Comment comment;
    private DatabaseController databaseController;
    private static CommentController instance;

    public CommentController() {
        this.databaseController = DatabaseController.getInstance();
    }

    public static CommentController getInstance() {
        if (instance == null) {
            instance = new CommentController();
        }
        return instance;
    }

    public String addComment(int userCommenterId, String description) {
        User user = databaseController.getUserById(userCommenterId);
        if(user == null) {
            return "User not found";
        }
        if(description == null || description.isEmpty()) {
            return "Comment cannot be empty";
        }
        Comment newComment = new Comment(userCommenterId , description , new Date());
        comment = newComment;
        return "Comment added successfully";
    }

    public String deleteComment(int userCommenterId) {
        if (comment.getUserCommenterId() != userCommenterId) {
            return "You are not allowed to delete this comment";
        }
        comment = null;
        return "Comment deleted successfully";
    }

    public String editComment(int userCommenterId, String description) {
       if (comment.getUserCommenterId() != userCommenterId) {
           return "You are not allowed to edit this comment";
       }
       if(description == null || description.isEmpty()) {
           return "Comment cannot be empty";
       }
       comment.setComment(description);
       comment.setCommentDate(LocalDate.now());
       return "Comment edited successfully";
    }
}
