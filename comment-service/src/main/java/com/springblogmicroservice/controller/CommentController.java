package com.springblogmicroservice.controller;

import com.springblogmicroservice.dto.payload.request.CommentRequest;
import com.springblogmicroservice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<?> saveComment(@Valid @RequestBody CommentRequest commentRequest,
                                         @RequestParam(value = "post-id")Long postId,
                                         @RequestParam(value = "comment-id")Long commentId,
                                         @RequestHeader (name="Authorization") String token){

        if( postId ==null || commentId ==null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        return ResponseEntity.ok().body(commentService.saveComment(commentRequest,token,postId,commentId));
    }

}
