package com.springblogmicroservice.service;


import com.springblogmicroservice.dto.PostListIdRequest;
import com.springblogmicroservice.dto.PostRequest;
import com.springblogmicroservice.entity.Post;
import com.springblogmicroservice.repository.PostRepository;
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


    @Override
    public Post savePost(PostRequest postRequest, Long userId) {

        //TODO wc post call tagIs is ok if its ok then wc post call write post-tag ids if its ok then write post with user-id

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


        Boolean res1 = webClient.post()
                .uri("http://localhost:8084/api/tag/check-tags")
                .body(Mono.just(requestPostIds),PostListIdRequest.class)
                .retrieve()
                .bodyToMono(Boolean.class).block();
        Boolean res2 = false;

        if(Boolean.TRUE.equals(res1)){
             res2 = webClient.post()
                    .uri("http://localhost:8085/api/post-tag/save")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(map1),HashMap.class)
                    .retrieve()
                    .bodyToMono(Boolean.class).block();
        }

        if(Boolean.TRUE.equals(res2)){
            Long res3 = webClient.get()
                    .uri("http://localhost:8082/auth/getuserid")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(Long.class).block();

            post.setUserId(res3);
            postRepository.saveAndFlush(post) ;
        }

        return post;

    }
}
