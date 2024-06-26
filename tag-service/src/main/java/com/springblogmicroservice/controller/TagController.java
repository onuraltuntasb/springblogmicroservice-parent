package com.springblogmicroservice.controller;

import com.springblogmicroservice.dto.PostIdRequest;
import com.springblogmicroservice.dto.PostListIdRequest;
import com.springblogmicroservice.entity.Tag;
import com.springblogmicroservice.repository.TagRepository;
import com.springblogmicroservice.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;
    private final TagService tagService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTag(@Valid @RequestBody Tag tag, @RequestParam(value = "user-id")Long userId){

        if(userId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        return ResponseEntity.ok().body(tagRepository.save(tag));
    }

    @PostMapping("/check-tags")
    public ResponseEntity<?> checkTags(@RequestBody List<PostListIdRequest> requestPostIds){

        if(requestPostIds.size() == 0){
            return ResponseEntity.badRequest().body(false);
        }

        return ResponseEntity.ok().body(tagService.checkTags(requestPostIds));
    }

}
