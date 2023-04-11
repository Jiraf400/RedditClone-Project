package com.jirafik.boot.mapper;

import com.jirafik.boot.dto.PostRequest;
import com.jirafik.boot.dto.PostResponse;
import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Post;
import com.jirafik.boot.entity.Subreddit;
import org.springframework.stereotype.Component;



@Component
public class PostMapperImpl extends PostMapper {

    @Override
    public Post map(PostRequest postRequest, Subreddit subreddit, Person person) {
        if (postRequest == null && subreddit == null && person == null)   return null;

        var post = Post.builder();

        if (postRequest != null) {
            post
            .postId(postRequest.getPostId())
            .postName(postRequest.getPostName())
            .url(postRequest.getUrl())
            .description(postRequest.getDescription());
        }
        if (subreddit != null) {
            post
            .person(person)
            .subreddit(subreddit);
        }
        post
        .createdDate(java.time.Instant.now())
        .voteCount(0);

        return post.build();
    }

    @Override
    public PostResponse mapToDto(Post post) {
        if(post == null) return null;

        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .url(post.getUrl())
                .subredditName(post.getSubreddit().getTitle())
                .userName(post.getPerson().getUsername())
                .commentCount(commentCount(post))
                .duration(getDuration(post))
                .build();
    }
}
