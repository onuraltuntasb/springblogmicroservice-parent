package com.springblogmicroservice.controller;



import com.springblogmicroservice.dto.payload.request.CheckAuthRequest;
import com.springblogmicroservice.dto.payload.request.TokenRefreshRequest;
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
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user){


        return ResponseEntity.ok(
                userService.setUserOtherParams(
                        userService.registerUser(
                                user),true
                )
        );
    }
    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody User user){

        return ResponseEntity.ok(
                userService.setUserOtherParams(
                        userService.loginUser(user),true
                )
        );
    }

    @PostMapping("/auth/check")
    public ResponseEntity<Boolean> checkAuth(@Valid @RequestBody CheckAuthRequest checkAuthRequest){
        return ResponseEntity.ok(jwtUtils.isTokenValid(checkAuthRequest.getToken(),checkAuthRequest.getEmail()));
    }

    //TODO hit from gateway when refresh token expired
    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
       return ResponseEntity.ok(userService.getTokenRefreshResponse(request));
    }



    @GetMapping("auth/getuserid")
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

    @GetMapping("auth/getusermail")
    public ResponseEntity<?> getUserMail(@RequestParam (name="user-id") Long userId){
        if(userId!=null){
            User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Not found user!"));
            return ResponseEntity.ok(user.getEmail());
        }else{
            return ResponseEntity.ok("Bad Request!");
        }
    }
}

