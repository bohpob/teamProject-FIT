package cz.cvut.fit.sp.chipin.base.notification.mapper;

import cz.cvut.fit.sp.chipin.base.notification.content.mapper.NotificationContentReadNotificationResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationReadNotificationResponse {
    private Long id;
    private NotificationContentReadNotificationResponse content;
    private LocalDateTime dateTime;
    private Boolean read;
}
