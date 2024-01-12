package cz.cvut.fit.sp.chipin.base.notification.content.mapper;

import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationContentMapper {
    NotificationReadNotificationResponse entityToReadNotificationResponse(NotificationContent notificationContent);
}

