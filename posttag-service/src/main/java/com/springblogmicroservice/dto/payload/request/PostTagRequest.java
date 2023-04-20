package com.springblogmicroservice.dto.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostTagRequest {
    private Long postId;
    private Long tagId;
}