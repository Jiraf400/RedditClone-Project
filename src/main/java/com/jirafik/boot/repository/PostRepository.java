package com.jirafik.boot.repository;

import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Post;
import com.jirafik.boot.entity.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByPerson(Person person);
}
