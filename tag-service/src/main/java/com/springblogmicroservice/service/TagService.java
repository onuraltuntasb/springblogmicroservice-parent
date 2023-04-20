package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.PostListIdRequest;


public interface TagService {
    boolean checkTags(PostListIdRequest checkTagIds);
}
