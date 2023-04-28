package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.payload.request.CommentRequest;
import com.springblogmicroservice.entity.Comment;
import com.springblogmicroservice.event.CommentNotificationEvent;
import com.springblogmicroservice.exception.ResourceNotFoundException;
import com.springblogmicroservice.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final WebClient webClient;
    private final KafkaTemplate<String, Comment> kafkaTemplate;


    @Transactional
    @Override
    public Comment saveComment(CommentRequest commentRequest,String token, Long postId, Long commentId) {


        //TODO get user id from auth-service
        //TODO check post id is real from post-service

        Long userId;

        try {
            userId = webClient.get()
                    .uri("http://localhost:8082/api/getuserid")
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
                                .queryParam("post-id",postId)
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
                    .userId(userId)
                    .postId(postId)
                    .build();

            Comment comment = null;


            if (commentId != 0) {
                comment = commentRepository.findById(commentId).orElseThrow(
                        ()->new ResourceNotFoundException("Comment not found with this id : "+ userId)
                );
                rComment.setCommentId(commentId);
                comment.setPostId(postId);
                comment.setUserId(userId);

            }else{
                rComment.setCommentId(0);
                rComment.setPostId(postId);
                rComment.setUserId(userId);

            }

            rComment = commentRepository.saveAndFlush(rComment);
            kafkaTemplate.send("notificationTopic",rComment);
            return rComment;

        }
        return null;
    }
}
