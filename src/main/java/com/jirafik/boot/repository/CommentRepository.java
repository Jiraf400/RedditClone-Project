package com.jirafik.boot.repository;

import com.jirafik.boot.entity.Comment;
import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByPerson(Person person);

}
