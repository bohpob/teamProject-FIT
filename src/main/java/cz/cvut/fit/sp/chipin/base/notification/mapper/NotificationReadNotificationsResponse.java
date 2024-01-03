package cz.cvut.fit.sp.chipin.base.notification.mapper;

import cz.cvut.fit.sp.chipin.base.notification.content.mapper.NotificationContentReadNotificationsResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationReadNotificationsResponse {
    private Long id;
    private NotificationContentReadNotificationsResponse content;
    private LocalDateTime dateTime;
    private Boolean read;
    private String userId;
    private String groupName;
    private Long transactionId;
}
