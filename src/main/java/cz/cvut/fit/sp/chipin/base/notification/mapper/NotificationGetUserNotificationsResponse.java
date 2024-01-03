package cz.cvut.fit.sp.chipin.base.notification.mapper;

import cz.cvut.fit.sp.chipin.base.notification.content.mapper.NotificationContentGetUserNotificationsResponse;
import lombok.Data;

@Data
public class NotificationGetUserNotificationsResponse {
    private Long id;
    private NotificationContentGetUserNotificationsResponse content;
    private Long timestamp;
    private Boolean read;
    private String userId;
    private String groupName;
    private Long transactionId;
}
