package com.springblogmicroservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.springblogmicroservice.dto.payload.request.TokenRefreshRequest;
import com.springblogmicroservice.dto.payload.response.TokenRefreshResponse;
import com.springblogmicroservice.dto.payload.response.UserAuthResponse;
import com.springblogmicroservice.entity.RefreshToken;
import com.springblogmicroservice.entity.User;
import com.springblogmicroservice.exception.ResourceNotFoundException;
import com.springblogmicroservice.exception.TokenRefreshException;
import com.springblogmicroservice.repository.RefreshTokenRepository;
import com.springblogmicroservice.repository.RoleRepository;
import com.springblogmicroservice.repository.UserRepository;
import com.springblogmicroservice.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.stream.Optional;
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
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;


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

}
