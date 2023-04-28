package com.springblogmicroservice.service;


import com.springblogmicroservice.dto.payload.request.FollowPostRequest;
import com.springblogmicroservice.dto.payload.request.TokenRefreshRequest;
import com.springblogmicroservice.dto.payload.response.TokenRefreshResponse;
import com.springblogmicroservice.dto.payload.response.UserAuthResponse;
import com.springblogmicroservice.entity.FollowedPosts;
import com.springblogmicroservice.entity.NotificationSettings;
import com.springblogmicroservice.entity.User;

public interface UserService {
    User registerUser(User user);
    User loginUser(User user);
    UserAuthResponse setUserOtherParams(User user, boolean authenticated);
    TokenRefreshResponse getTokenRefreshResponse(TokenRefreshRequest request);
//    User updateUser(User user,Long userId);
//    void deleteUser(Long userId);

    FollowedPosts saveFollowPost(FollowedPosts followedPosts);

    NotificationSettings saveNotificationSettings(NotificationSettings notificationSettings,String token);

}
