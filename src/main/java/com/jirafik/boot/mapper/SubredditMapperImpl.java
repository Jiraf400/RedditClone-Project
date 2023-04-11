package com.jirafik.boot.mapper;

import com.jirafik.boot.dto.SubredditDto;

import com.jirafik.boot.entity.Subreddit;
import com.jirafik.boot.security.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class SubredditMapperImpl implements SubredditMapper {

    private final AuthenticationService authenticationService;

    @Override
    public SubredditDto mapSubredditToDto(Subreddit subreddit) {
        if (subreddit == null) return null;

        return SubredditDto.builder()
                .id(subreddit.getId())
                .title(subreddit.getTitle())
                .description(subreddit.getDescription())
                .numberOfPosts(mapPosts(subreddit.getPosts()))
                .build();
    }

    @Override
    public Subreddit mapDtoToSubreddit(SubredditDto dto) {
        if (dto == null) return null;

        return Subreddit.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .createdDate(Instant.now())
                .person(authenticationService.getCurrentUser())
                .build();
    } 
}
