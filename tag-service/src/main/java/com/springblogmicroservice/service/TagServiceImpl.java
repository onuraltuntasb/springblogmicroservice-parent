package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.PostListIdRequest;
import com.springblogmicroservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    @Override
    public boolean checkTags(List<PostListIdRequest> checkTagIds) {

        List<Long> tagIds = tagRepository.getAllIdsFromTable();
        boolean isOk = false;

       for (PostListIdRequest cId : checkTagIds) {
           isOk = tagIds.contains(cId.getId());
       };

        return isOk;
    }
}
