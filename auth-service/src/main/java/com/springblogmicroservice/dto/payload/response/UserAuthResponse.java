package com.springblogmicroservice.dto.payload.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Data
@Builder
public class UserAuthResponse {

    private String email;
    private String name;
    private Collection<? extends GrantedAuthority> authorities;
    private String jwtToken;
    private String jwtRefreshToken;

}
