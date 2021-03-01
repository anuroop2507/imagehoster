package ImageHoster.service;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    //The method calls the createComment() method in the Repository and passes the comment to be persisted in the database
    public void createComment(Comment newComment) {
        commentRepository.createComment(newComment);
    }

    //The method calls the getAllCommentOfImage method in the Repository and passes the image for which
    //the comments need to be fetched
    public List<Comment> getAllCommentOfImage(Image image) throws IOException {
        List<Comment> comments = commentRepository.getAllCommentsOfImage(image);
        for (Comment c: comments) {
            c.setText(decodeCommentToString(c.getText()));
        }
        return comments;
    }

    //Decoded the comments form Base64 to string
    private String decodeCommentToString(String comment) throws IOException {
        return new String(Base64.getDecoder().decode(comment));
    }
}
