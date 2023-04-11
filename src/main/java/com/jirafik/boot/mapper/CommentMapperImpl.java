package com.jirafik.boot.mapper;

import com.jirafik.boot.dto.CommentDto;
import com.jirafik.boot.entity.Comment;
import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Post;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public Comment map(CommentDto commentDto, Post post, Person person) {
        return Comment.builder()
                .text(commentDto.getText())
                .createdDate(Instant.now())
                .post(post)
                .person(person)
                .build();
    }

    @Override
    public CommentDto mapToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .createdDate(Instant.now())
                .text(comment.getText())
                .postId(comment.getPost().getPostId())
                .createdBy(comment.getPerson().getUsername())
                .build();
    }
}
