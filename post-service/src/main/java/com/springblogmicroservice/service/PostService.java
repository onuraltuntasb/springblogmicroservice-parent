package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.PostRequest;
import com.springblogmicroservice.entity.Post;

public interface PostService {
    //TODO post_tag service call post --> post_tag --> write to db
    Post savePost(PostRequest postRequest, Long userId);
}
