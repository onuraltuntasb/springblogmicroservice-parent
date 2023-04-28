package com.springblogmicroservice.controller;

import com.springblogmicroservice.dto.PostRequest;
import com.springblogmicroservice.entity.Post;
import com.springblogmicroservice.exception.ResourceNotFoundException;
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

    @GetMapping("/check")
    public ResponseEntity<?> savePost(@RequestParam(value = "post-id")Long postId){


        if(postId == null ){
            return ResponseEntity.badRequest().body(false);
        }

         Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("There is no post with this id : "+ postId));

        if(post!=null){
            return ResponseEntity.ok().body(true);
        }else{
            return ResponseEntity.ok().body(false);
        }
    }

}
