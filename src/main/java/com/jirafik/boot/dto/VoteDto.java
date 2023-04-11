package com.jirafik.boot.dto;

import com.jirafik.boot.entity.VoteType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private VoteType voteType;
    private Long postId;
}
