package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.PostListIdRequest;
import java.util.List;


public interface TagService {
    boolean checkTags(List<PostListIdRequest> checkTagIds);
}
