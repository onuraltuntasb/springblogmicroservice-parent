package com.springblogmicroservice.controller;

import com.springblogmicroservice.entity.FollowedPosts;
import com.springblogmicroservice.entity.NotificationSettings;
import com.springblogmicroservice.entity.User;
import com.springblogmicroservice.exception.ResourceNotFoundException;
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
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @PostMapping("/follow-post")
    public ResponseEntity<?> saveFollowPost(@Valid @RequestBody FollowedPosts followedPosts) {
        return ResponseEntity.ok(userService.saveFollowPost(followedPosts));
    }

    @PostMapping("/notification-settings")
    public ResponseEntity<?> saveNotificationSettings(@Valid @RequestBody NotificationSettings notificationSettings,
                                                     @RequestHeader (name="Authorization") String token) {



        return ResponseEntity.ok(userService.saveNotificationSettings(notificationSettings,token));
    }

}
