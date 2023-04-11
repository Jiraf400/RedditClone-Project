package com.jirafik.boot.controller;

import com.jirafik.boot.dto.SubredditDto;
import com.jirafik.boot.entity.Subreddit;
import com.jirafik.boot.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/main/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService service;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto dto) {
        return ResponseEntity.status(CREATED).body(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity.status(OK).body(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id){
        return ResponseEntity.status(OK).body(service.getSubreddit(id));
    }


}






















