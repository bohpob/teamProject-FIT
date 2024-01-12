package cz.cvut.fit.sp.chipin.base.notification.content.mapper;

import lombok.Data;

@Data
public class NotificationContentReadNotificationResponse {
    private Long id;
    private String title;
    private String text;
}
