package com.jirafik.boot.repository;

import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Post;
import com.jirafik.boot.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndPersonOrderByVoteIdDesc(Post post, Person currentUser);
}
