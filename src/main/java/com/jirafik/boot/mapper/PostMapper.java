package com.jirafik.boot.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jirafik.boot.dto.PostRequest;
import com.jirafik.boot.dto.PostResponse;
import com.jirafik.boot.entity.*;
import com.jirafik.boot.repository.CommentRepository;
import com.jirafik.boot.repository.VoteRepository;
import com.jirafik.boot.security.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.jirafik.boot.entity.VoteType.DOWNVOTE;
import static com.jirafik.boot.entity.VoteType.UPVOTE;

public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthenticationService authService;

    //Post from PostRequest
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, Person person);

    //PostResponse from Post
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndPersonOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;

    }
}
