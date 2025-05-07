package com.skillup.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.demo.dto.UserDto;
import com.skillup.demo.exception.CommentException;
import com.skillup.demo.exception.PostException;
import com.skillup.demo.exception.UserException;
import com.skillup.demo.model.Comments;
import com.skillup.demo.model.Post;
import com.skillup.demo.model.User;
import com.skillup.demo.repository.CommentRepository;
import com.skillup.demo.repository.PostRepository;

@Service
public class CommentsServiceImplement implements CommentService {

    @Autowired
    private CommentRepository repo;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private PostRepository postRepo;

    @Override
    public Comments createComment(Comments comment, Integer postId, Integer userId) throws PostException, UserException {
        User user = userService.findUserById(userId);
        Post post = postService.findePostById(postId);
        
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        
        comment.setUserDto(userDto);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        
        Comments newComment = repo.save(comment);
        
        post.getComments().add(newComment);
        postRepo.save(post);
        
        return newComment;
    }

    @Override
    public Comments findCommentById(Integer commentId) throws CommentException {
        return repo.findById(commentId)
                .orElseThrow(() -> new CommentException("Comment not exist with id: " + commentId));
    }

    @Override
    public Comments likeComment(Integer commentId, Integer userId) throws UserException, CommentException {
        User user = userService.findUserById(userId);
        Comments comment = findCommentById(commentId);
        
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setUserImage(user.getImage());
        
        // Check if user already liked the comment
        boolean alreadyLiked = comment.getLikedByUsers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        
        if (!alreadyLiked) {
            comment.getLikedByUsers().add(userDto);
            return repo.save(comment);
        }
        
        return comment;
    }

    @Override
    public Comments unlikeComment(Integer commentId, Integer userId) throws UserException, CommentException {
        Comments comment = findCommentById(commentId);
        
        // Remove if user exists in likedByUsers
        comment.getLikedByUsers().removeIf(u -> u.getId().equals(userId));
        
        return repo.save(comment);
    }

    @Override
    public String deleteCommentById(Integer commentId) throws CommentException {
        Comments comment = findCommentById(commentId);
        
        // Remove comment from post's comment list first
        Post post = comment.getPost();
        if (post != null) {
            post.getComments().removeIf(c -> c.getId().equals(commentId));
            postRepo.save(post);
        }
        
        repo.delete(comment);
        return "Comment Deleted Successfully";
    }

    @Override
    public String editComment(Comments updatedComment, Integer commentId) throws CommentException {
        Comments existingComment = findCommentById(commentId);
        
        if (updatedComment.getContent() != null) {
            existingComment.setContent(updatedComment.getContent());
        }
        
        repo.save(existingComment);
        return "Comment Updated Successfully";
    }

    @Override
    public List<Comments> findCommentByPostId(Integer postId) throws PostException {
        return repo.findCommentsByPostId(postId);
    }
}
