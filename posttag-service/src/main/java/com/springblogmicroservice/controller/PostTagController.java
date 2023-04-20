package com.springblogmicroservice.controller;

import com.springblogmicroservice.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post-tag")
@RequiredArgsConstructor
public class PostTagController {

    PostTagRepository postTagRepository;

    @PostMapping("/save")
    public ResponseEntity<?> savePost(@RequestParam(value = "post-id")Long postId,
                                      @RequestParam(value = "tag-id")Long tagId){

        if(postId == null || tagId ==null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        postTagRepository.savePostTag(postId,tagId);

        return ResponseEntity.ok().body("success");
    }




}
