package com.jirafik.boot.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDto {
    private Long id;
    private String title;
    private String description;
    private Integer numberOfPosts;
}
