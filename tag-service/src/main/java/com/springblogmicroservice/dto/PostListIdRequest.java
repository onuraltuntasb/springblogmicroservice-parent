package com.springblogmicroservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostListIdRequest {
    private List<PostIdRequest> tags;
}
