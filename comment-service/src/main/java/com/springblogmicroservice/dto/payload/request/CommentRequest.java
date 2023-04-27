package com.springblogmicroservice.dto.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CommentRequest {
    private String content;
    private Date creationDate;
    private Date lastUpdateDate;
}

