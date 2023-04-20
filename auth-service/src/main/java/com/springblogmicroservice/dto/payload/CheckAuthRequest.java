package com.springblogmicroservice.dto.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckAuthRequest {
    private String token;
    private String email;
}
