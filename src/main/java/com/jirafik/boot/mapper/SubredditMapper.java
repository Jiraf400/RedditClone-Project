package com.jirafik.boot.mapper;

import com.jirafik.boot.dto.SubredditDto;
import com.jirafik.boot.entity.Post;
import com.jirafik.boot.entity.Subreddit;

import java.util.List;

public interface SubredditMapper {

    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
