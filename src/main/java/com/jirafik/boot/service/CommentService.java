package com.jirafik.boot.service;

import com.jirafik.boot.dto.CommentDto;
import com.jirafik.boot.entity.Comment;
import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Post;
import com.jirafik.boot.mapper.CommentMapper;
import com.jirafik.boot.repository.CommentRepository;
import com.jirafik.boot.repository.PersonRepository;
import com.jirafik.boot.repository.PostRepository;
import com.jirafik.boot.security.auth.AuthenticationService;
import com.jirafik.boot.security.mailSender.MailContentBuilder;
import com.jirafik.boot.security.mailSender.MailService;
import com.jirafik.boot.security.mailSender.NotificationEmail;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final PersonRepository personRepository;
    private final AuthenticationService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getPerson().getUsername() +
                " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getPerson());
    }

    private void sendCommentNotification(String message, Person person) {
        mailService.sendMail(new NotificationEmail(person.getUsername() +
                " Commented on your post", person.getEmail(), message));
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new RuntimeException("Post not found with id: " + postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).toList();
    }

    //TODO AAAAAAAAAAAAAAAAAAAAA
    public List<CommentDto> getAllCommentsForPerson(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return commentRepository.findAllByPerson(person)
                .stream()
                .map(commentMapper::mapToDto)
                .toList();
    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new RuntimeException("Comments contains unacceptable language");
        }
        return false;
    }
}
