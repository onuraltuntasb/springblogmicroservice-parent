package org.springblogmicroservice;

import lombok.RequiredArgsConstructor;
import org.springblogmicroservice.event.Comment;
import org.springblogmicroservice.service.NotificationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
public class NotificationServiceApplication {

    private final NotificationService notificationService;


    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(Comment comment){

        System.out.println("Getting notification as : " + comment);

        notificationService.processComment(comment);

        //send mail after processing
    }

}
