package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.Tag;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;


    //This method is used to create a comment on an image and then redirect to the same page and displayes all the
    //comments of that image
    @RequestMapping(value = "/image/{id}/{title}/comments", method = RequestMethod.POST)
    public String createComment (@PathVariable("id") Integer imageId,
                                 @PathVariable("title") String imageTitle,
                                 @RequestParam("comment") String comment,
                                 Comment newComment,
                                 HttpSession session,
                                 Model model) throws IOException {
        Image image = imageService.getImage(imageId);
        User user = (User) session.getAttribute("loggeduser");
        newComment.setImage(image);
        newComment.setUser(user);
        newComment.setCreatedDate(LocalDate.now());
        String commentText = convertCommentToBase64(comment);
        newComment.setText(commentText);
        commentService.createComment(newComment);

        List<Tag> tags = image.getTags();
        //return all the comments of an image
        List<Comment> comments = commentService.getAllCommentOfImage(image);
        model.addAttribute("image", image);
        model.addAttribute("tags", tags);
        model.addAttribute("comments",comments);
        return "redirect:/images/" + image.getId() + "/" + image.getTitle();
    }

    //This method converts the comment to Base64 format
    private String convertCommentToBase64(String comment) throws IOException {
        return Base64.getEncoder().encodeToString(comment.getBytes());
    }

}
