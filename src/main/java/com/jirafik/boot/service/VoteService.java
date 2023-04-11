package com.jirafik.boot.service;

import com.jirafik.boot.dto.VoteDto;
import com.jirafik.boot.entity.Post;
import com.jirafik.boot.entity.Vote;
import com.jirafik.boot.repository.PostRepository;
import com.jirafik.boot.repository.VoteRepository;
import com.jirafik.boot.security.auth.AuthenticationService;
import jakarta.mail.IllegalWriteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.jirafik.boot.entity.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthenticationService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post Not Found with ID - " + voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser =
                voteRepository.findTopByPostAndPersonOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new RuntimeException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (voteDto.getVoteType().equals(UPVOTE)) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }


        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .person(authService.getCurrentUser())
                .build();
    }
}
