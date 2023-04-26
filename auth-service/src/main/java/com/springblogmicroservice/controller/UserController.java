package com.springblogmicroservice.controller;



import com.springblogmicroservice.dto.payload.CheckAuthRequest;
import com.springblogmicroservice.dto.payload.TokenRefreshRequest;
import com.springblogmicroservice.entity.User;
import com.springblogmicroservice.exception.ResourceNotFoundException;
import com.springblogmicroservice.repository.UserRepository;
import com.springblogmicroservice.security.JwtUtils;
import com.springblogmicroservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user){


        return ResponseEntity.ok(
                userService.setUserOtherParams(
                        userService.registerUser(
                                user),true
                )
        );
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody User user){

        return ResponseEntity.ok(
                userService.setUserOtherParams(
                        userService.loginUser(user),true
                )
        );
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> checkAuth(@Valid @RequestBody CheckAuthRequest checkAuthRequest){
        return ResponseEntity.ok(jwtUtils.isTokenValid(checkAuthRequest.getToken(),checkAuthRequest.getEmail()));
    }

    //TODO hit from gateway when refresh token expired
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
       return ResponseEntity.ok(userService.getTokenRefreshResponse(request));
    }

    @GetMapping("/getuserid")
    public ResponseEntity<?> getUserId(@RequestHeader (name="Authorization") String token){
        String email = "";
        System.out.println("token : "+ token);
        if(token !=null){
            email  =  jwtUtils.extractUsername(token.substring(7));
            Long userId = userRepository.findUserByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Not found user!")).getId();
            return ResponseEntity.ok(userId);
        }else{
            return ResponseEntity.ok("Bad Request!");
        }
    }
}

