package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.PostListIdRequest;
import com.springblogmicroservice.dto.PostRequest;
import com.springblogmicroservice.entity.Post;
import com.springblogmicroservice.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final WebClient webClient;
    private final PostRepository postRepository;


    @Transactional
    @Override
    public Post savePost(PostRequest postRequest,String token) {

        //TODO transactional seems working ok

        List<PostListIdRequest> requestPostIds = postRequest.getTags();
        HashMap<Long,Long> map1 = new HashMap<>();

        Date date = new Date();

        Post post = Post.builder()
                .header(postRequest.getHeader())
                .content(postRequest.getContent())
                .creationDate(date)
                .lastUpdateDate(date)
                .likeCount(0)
                .dislikeCount(0)
                .userId(1L)
                .popular(0.0)
                .build();

        Post savedPost = postRepository.saveAndFlush(post) ;
        Long savedPostId = savedPost.getId();

        for (PostListIdRequest el : requestPostIds) {
            map1.put(savedPostId, el.getId());
        }

        Boolean res1;
        Boolean res2 = false;
        try {
             res1 = webClient.post()
                    .uri("http://localhost:8084/api/tag/check-tags")
                    .body(Mono.just(requestPostIds),PostListIdRequest.class)
                    .retrieve()
                    .bodyToMono(Boolean.class).block();

        }catch (Exception e){
            throw new RuntimeException("Some error occurred when calling posttag service!");
        }

        System.out.println("res1 is :"+res1);

        if(Boolean.TRUE.equals(res1)){
            try {
                res2 = webClient.post()
                        .uri("http://localhost:8085/api/post-tag/save")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(Mono.just(map1),HashMap.class)
                        .retrieve()
                        .bodyToMono(Boolean.class).block();
            }catch (Exception e){
                throw new RuntimeException("Some error occurred when calling posttag service!");
            }
        }

        System.out.println("res2 is :"+res2);

        if(Boolean.TRUE.equals(res2)){
            try {
                Long res3 = webClient.get()
                        .uri("http://localhost:8082/api/auth/getuserid")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .retrieve()
                        .bodyToMono(Long.class).block();

                post.setUserId(res3);
                postRepository.saveAndFlush(post) ;
            }catch (Exception e){
                throw new RuntimeException("Some error occurred when getting user id from auth server!");
            }
        }

        return post;

    }
}
