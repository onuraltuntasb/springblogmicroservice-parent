package com.springblogmicroservice.dto.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FollowPostRequest {

    @NotBlank
    private Long userId;
    @NotBlank
    private Long postId;

}

