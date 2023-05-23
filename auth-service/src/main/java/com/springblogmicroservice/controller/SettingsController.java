package com.springblogmicroservice.controller;

import com.springblogmicroservice.entity.FollowedPosts;
import com.springblogmicroservice.entity.NotificationSettings;
import com.springblogmicroservice.entity.User;
import com.springblogmicroservice.exception.ResourceNotFoundException;
import com.springblogmicroservice.repository.NotificationSettingsRepository;
import com.springblogmicroservice.repository.UserRepository;
import com.springblogmicroservice.security.JwtUtils;
import com.springblogmicroservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SettingsController {


    private final UserService userService;
    private final UserRepository userRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;

    @PostMapping("/follow-post")
    public ResponseEntity<?> saveFollowPost(@RequestParam(value = "post-id")Long postId,
                                            @RequestHeader (name="Authorization") String token) {
        return ResponseEntity.ok(userService.saveFollowPost(postId,token));
    }

    @GetMapping("/auth/follow-post")
    public ResponseEntity<?> getFollowPosts(@RequestParam(value = "user-id")Long userId){

        if(userId !=null){
            User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found with this id:" + userId));
            return ResponseEntity.ok(user.getFollowedPosts());
        }else{
            return ResponseEntity.ok("Bad Request!");
        }
    }

    @PostMapping("/notification-settings")
    public ResponseEntity<?> saveNotificationSettings(@Valid @RequestBody NotificationSettings notificationSettings,
                                                     @RequestHeader (name="Authorization") String token) {

        return ResponseEntity.ok(userService.saveNotificationSettings(notificationSettings,token));
    }

    @GetMapping("/auth/notification-settings")
    public ResponseEntity<?> getNotificationSettings(@RequestParam(value = "user-id")Long userId){

        if(userId !=null){
            User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found with this id:" + userId));
            NotificationSettings notificationSettings = user.getNotificationSettings();
            return ResponseEntity.ok(notificationSettings);
        }else{
            return ResponseEntity.ok("Bad Request!");
        }
    }

}
