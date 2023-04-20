package com.springblogmicroservice.service;


import com.springblogmicroservice.dto.PostIdRequest;
import com.springblogmicroservice.dto.PostListIdRequest;
import com.springblogmicroservice.entity.Tag;
import com.springblogmicroservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    @Override
    public boolean checkTags(PostListIdRequest checkTagIds) {

        List<Long> tagIds = tagRepository.getAllIdsFromTable();
        boolean isOk = false;

       for (PostIdRequest cId : checkTagIds.getTags()) {
           isOk = tagIds.contains(cId.getId());
       };

        return isOk;
    }
}
