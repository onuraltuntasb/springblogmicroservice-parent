package org.springblogmicroservice.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationSettings {
    private Long id;
    private boolean postComment;
    private boolean commentReply;

}
