package com.jirafik.boot.mapper;

import com.jirafik.boot.dto.CommentDto;
import com.jirafik.boot.entity.Comment;
import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Post;

public interface CommentMapper {

    //CommentDto to Comment
    Comment map(CommentDto commentDto, Post post, Person person);

    //Comment to CommentDto
    CommentDto mapToDto(Comment comment);
}
