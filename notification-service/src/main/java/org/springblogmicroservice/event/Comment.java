package org.springblogmicroservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    private Long id;

    private Date creationDate;

    private Date lastUpdateDate;

    private String content;

    private int likeCount;

    private int dislikeCount;

    private long userId;

    private long postId;

    private long commentId;
}