package com.springblogmicroservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.springblogmicroservice.dto.payload.request.FollowPostRequest;
import com.springblogmicroservice.dto.payload.request.TokenRefreshRequest;
import com.springblogmicroservice.dto.payload.response.TokenRefreshResponse;
import com.springblogmicroservice.dto.payload.response.UserAuthResponse;
import com.springblogmicroservice.entity.FollowedPosts;
import com.springblogmicroservice.entity.NotificationSettings;
import com.springblogmicroservice.entity.RefreshToken;
import com.springblogmicroservice.entity.User;
import com.springblogmicroservice.exception.ResourceNotFoundException;
import com.springblogmicroservice.exception.TokenRefreshException;
import com.springblogmicroservice.repository.*;
import com.springblogmicroservice.security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final FollowPostRepository followPostRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;


    @Transactional
    @Override
    public User registerUser(User user) {

        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        //set other user fields
        user.setStatus(User.UserStatus.ACTIVE);
        user.setPhone(user.getPhone());

        //encode password
        String plainPassword = user.getPassword();
        if(plainPassword!=null){
            user.setPassword(passwordEncoder.encode(plainPassword));
        }else{
            throw new ResourceNotFoundException("User password not found!");
        }
            RefreshToken refreshToken = refreshTokenService
                .createRefreshToken();

            user.setRefreshToken(refreshToken);

           NotificationSettings notificationSettings = NotificationSettings.builder().commentReply(true).commentReply(true).build();
           user.setNotificationSettings(notificationSettings);
           notificationSettingsRepository.save(notificationSettings);

        return  userRepository.save(user);
    }

    @Override
    public User loginUser(User user) {

        //find user
        User rUser = (User) userRepository
                .findUserByEmail(user.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException
                                ("Not found email with id = " + user.getEmail()));

        //check username
        if(!rUser.getName().equals(user.getName())){
            //just throw bad credentials for security purposes
            log.error("username is wrong!");
            throw new BadCredentialsException("Bad credentials!");
        }

        //authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),user.getPassword()
                ));


        return rUser;
    }

    public UserAuthResponse setUserOtherParams(User user, boolean authenticated){

        //find userDetails
        UserDetails userDetails = (UserDetails) userRepository
                .findUserByEmail(user.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Not found email with id = " + user.getEmail()));

        try {


            return UserAuthResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .authorities(user.getAuthorities())
                    .jwtToken(jwtUtils.generateToken(userDetails))
                    .jwtRefreshToken(user.getRefreshToken().getToken())
                    .build();
        }catch (NullPointerException e){
            log.error("userDetails or user possibly null!");
            return null;
        }
    }

    @Override
    public TokenRefreshResponse getTokenRefreshResponse(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();



        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken).orElseThrow(()-> new ResourceNotFoundException(
                "Not found user with this refresh token!"));


        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Not found user with this refresh token!"));

        if(refreshTokenService.verifyExpiration(refreshToken)){
           String token = jwtUtils.generateToken(user);
           return new TokenRefreshResponse(token, requestRefreshToken);
        }else{
             throw new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!");
        }
    }

    @Override
    public FollowedPosts saveFollowPost(Long postId,String token) {

        String email = "";
        System.out.println("token : "+ token);
        User user = null;
        FollowedPosts f = null;
        if(token !=null){
            email  =  jwtUtils.extractUsername(token.substring(7));
            user = userRepository.findUserByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Not found user!"));
            if(user.getId() !=null){
                 f =  FollowedPosts.builder().postId(postId).build();
            }
        }
        if(f == null){
            throw  new RuntimeException("followedPosts is null");
        }else{
            user.getFollowedPosts().add(f);
            return followPostRepository.save(f);

        }
    }

    @Transactional
    @Override
    public NotificationSettings saveNotificationSettings(NotificationSettings notificationSettings,String token) {

        String email = "";
        System.out.println("token : "+ token);
        if(token !=null){
            email  =  jwtUtils.extractUsername(token.substring(7));
            User user = userRepository.findUserByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Not found user!"));
            notificationSettingsRepository.findById(1L)
                    .orElseThrow(()-> new ResourceNotFoundException("Not found notification settings!"));

            user.setNotificationSettings(notificationSettings);
            userRepository.save(user);
        }else{
            throw new RuntimeException("token is coming null!");
        }
        notificationSettings.setId(1L);
        return notificationSettingsRepository.save(notificationSettings);
    }

}
