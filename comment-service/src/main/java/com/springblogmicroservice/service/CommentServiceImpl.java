package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.payload.request.CommentRequest;
import com.springblogmicroservice.entity.Comment;
import com.springblogmicroservice.exception.ResourceNotFoundException;
import com.springblogmicroservice.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final WebClient webClient;


    @Transactional
    @Override
    public Comment saveComment(CommentRequest commentRequest,String token, Long PostId, Long commentId) {


        //TODO get user id from auth-service
        //TODO check post id is real from post-service

        Long userId;

        try {
            userId = webClient.get()
                    .uri("http://localhost:8082/api/auth/getuserid")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(Long.class).block();

        }catch (Exception e){
            throw new RuntimeException("Some error occurred when getting user id from auth server!");
        }

        Boolean res2 = false;

        if(userId!=null){

                res2 = WebClient.create("http://localhost:8083")
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/post/check")
                                .queryParam("post-id",PostId)
                                .build())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .retrieve()
                        .bodyToMono(Boolean.class).block();


        }

        if(Boolean.TRUE.equals(res2)){
            Date date = new Date();

            Comment rComment = Comment.builder()
                    .content(commentRequest.getContent())
                    .creationDate(date)
                    .lastUpdateDate(date)
                    .dislikeCount(0)
                    .likeCount(0)
                    .build();

            Comment comment = null;


            if (commentId != 0) {
                comment = commentRepository.findById(commentId).orElseThrow(
                        ()->new ResourceNotFoundException("Comment not found with this id : "+ userId)
                );
                comment.setCommentId(commentId);
                comment.setPostId(PostId);
                comment.setUserId(userId);
            }else{
                rComment.setCommentId(0);
                rComment.setPostId(PostId);
                rComment.setUserId(userId);

                return commentRepository.save(rComment);
            }


        }
        return null;
    }
}
