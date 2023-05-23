package org.springblogmicroservice.service;

import lombok.RequiredArgsConstructor;
import org.springblogmicroservice.dto.model.FollowedPosts;
import org.springblogmicroservice.dto.model.GenericMailTemplate;
import org.springblogmicroservice.dto.model.NotificationSettings;
import org.springblogmicroservice.event.Comment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Override
    public GenericMailTemplate processComment(Comment comment) {

        //get user settings from authService
        //get user followed posts from authService

        NotificationSettings notificationSettings = null;
        Set<FollowedPosts> followedPosts = new HashSet<>();


        try {
             notificationSettings = WebClient.create("http://localhost:8082")
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/auth/notification-settings")
                            .queryParam("user-id",comment.getUserId())
                            .build())
                    .retrieve()
                    .bodyToMono(NotificationSettings.class).block();

        }catch (Exception e){
            throw new RuntimeException("Some error occurred when getting notification settings from auth server!");
        }

        try {
            followedPosts = WebClient.create("http://localhost:8082")
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/auth/follow-post")
                            .queryParam("user-id",comment.getUserId())
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<HashSet<FollowedPosts>>() {}).block();

        }catch (Exception e){
            throw new RuntimeException("Some error occurred when getting notification settings from auth server!");
        }


        //get user mail from id
        String userMail = "";

        try {
            userMail = WebClient.create("http://localhost:8082")
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/auth/getusermail")
                            .queryParam("user-id",comment.getUserId())
                            .build())
                    .retrieve()
                    .bodyToMono(String.class).block();

        }catch (Exception e){
            throw new RuntimeException("Some error occurred when getting user email from auth server!");
        }

        //TODO comment can filter before sending as notification
        //notification logic
        //comment is parent not reply
        GenericMailTemplate commentMail = null;
        String parentCommentMailText = "New comment added to your followed post, check it out now!";
        String replyCommentMailText = "New reply comment added to your followed post, check it out now!";

        if(notificationSettings !=null && followedPosts!=null && !userMail.isEmpty()){
            if(comment.getCommentId() == 0){
                if(notificationSettings.isPostComment()){
                    for (FollowedPosts f :followedPosts ) {
                        if(comment.getPostId() == f.getPostId()){

                            //TODO logic mistake get follow users and send them not comment owner

                            commentMail = GenericMailTemplate.builder()
                                    .To(userMail)
                                    .Subject("new comment!")
                                    .From("springblogmicro@gmail.com")
                                    .Text(parentCommentMailText)
                                    .build();
                        }
                    }
                }
            }else {
                if(notificationSettings.isPostComment() && notificationSettings.isCommentReply()){
                    for (FollowedPosts f :followedPosts ) {
                        if(comment.getPostId() == f.getPostId()){

                            commentMail = GenericMailTemplate.builder()
                                    .To(userMail)
                                    .Subject("new comment!")
                                    .From("springblogmicro@gmail.com")
                                    .Text(replyCommentMailText)
                                    .build();
                        }
                    }
                }
            }
        }else{
            throw new RuntimeException("notification settings is null!");
        }


        System.out.println(comment.getCommentId());
        System.out.println(notificationSettings);
        System.out.println(followedPosts);
        System.out.println(commentMail);

        return null;
    }
}
