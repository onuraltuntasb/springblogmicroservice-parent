package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.PostRequest;
import com.springblogmicroservice.entity.Post;

public interface PostService {
    Post savePost(PostRequest postRequest,String token);
}
