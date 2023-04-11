package com.jirafik.boot.service;

import com.jirafik.boot.dto.PostRequest;
import com.jirafik.boot.dto.PostResponse;
import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Post;
import com.jirafik.boot.entity.Subreddit;
import com.jirafik.boot.mapper.PostMapper;
import com.jirafik.boot.repository.PersonRepository;
import com.jirafik.boot.repository.PostRepository;
import com.jirafik.boot.repository.SubredditRepository;
import com.jirafik.boot.security.auth.AuthenticationService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final PersonRepository personRepository;
    private final AuthenticationService authService;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByTitle(postRequest.getSubredditName())
                .orElseThrow(() -> new RuntimeException("Subreddit not found: " + postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new RuntimeException("Subreddit not found with id: " + subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByEmail(String email) {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return postRepository.findByPerson(person)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
