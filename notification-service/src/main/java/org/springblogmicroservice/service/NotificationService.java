package org.springblogmicroservice.service;

import org.springblogmicroservice.dto.model.GenericMailTemplate;
import org.springblogmicroservice.event.Comment;
import org.springframework.stereotype.Service;


public interface NotificationService {
    //TODO actually, for real project we can use different body model for each services like factory pattern or library(like sendgrid)
    GenericMailTemplate processComment(Comment comment);
}
