package com.springblogmicroservice.controller;

import com.springblogmicroservice.dto.PostRequest;
import com.springblogmicroservice.repository.PostRepository;
import com.springblogmicroservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final ModelMapper modelMapper;

    @PostMapping("/save")
    public ResponseEntity<?> savePost(@Valid @RequestBody PostRequest postRequest,
                                      @RequestHeader (name="Authorization") String token){

        if(postRequest == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        return ResponseEntity.ok().body(postService.savePost(postRequest,token));
    }


}
