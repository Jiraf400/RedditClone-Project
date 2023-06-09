package com.jirafik.boot.controller;

import com.jirafik.boot.dto.VoteDto;
import com.jirafik.boot.service.VoteService;
import jakarta.mail.IllegalWriteException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/main/votes")
@AllArgsConstructor
public class VoteController {

    //TODO find why post.getVoteCount() is null

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto){
        voteService.vote(voteDto);
        return new ResponseEntity<>(OK);
    }

}























