package com.springblogmicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "notification_settings")
public class NotificationSettings {

    //TODO each post may has own notification settings (one to many settings to followed post)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //when comment sent under post generic
    private boolean postComment;

    //when comment is replied
    private boolean commentReply;

    

    //TODO when someone tagged (includes @ -> check user_name must be unique)


}
