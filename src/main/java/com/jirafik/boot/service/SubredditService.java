package com.jirafik.boot.service;

import com.jirafik.boot.dto.SubredditDto;
import com.jirafik.boot.entity.Subreddit;
import com.jirafik.boot.mapper.SubredditMapper;
import com.jirafik.boot.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository repository;
    private final SubredditMapper subredditMapper;


    @Transactional
    public SubredditDto save(SubredditDto dto) {
        Subreddit savedSub = repository.save(subredditMapper.mapDtoToSubreddit(dto));
        dto.setId(savedSub.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
         return repository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id){
        Subreddit subreddit = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
        return subredditMapper.mapSubredditToDto(subreddit);
    }


}
