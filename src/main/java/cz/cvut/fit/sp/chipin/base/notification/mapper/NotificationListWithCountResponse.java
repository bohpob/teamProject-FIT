package cz.cvut.fit.sp.chipin.base.notification.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotificationListWithCountResponse {
    private Long count;
    private List<NotificationReadNotificationsResponse> notifications;
}
