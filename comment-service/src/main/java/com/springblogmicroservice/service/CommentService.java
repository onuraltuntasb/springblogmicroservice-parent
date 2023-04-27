package com.springblogmicroservice.service;

import com.springblogmicroservice.dto.payload.request.CommentRequest;
import com.springblogmicroservice.entity.Comment;

public interface CommentService {
    Comment saveComment(CommentRequest commentRequest, String token, Long PostId, Long commentId);
}
