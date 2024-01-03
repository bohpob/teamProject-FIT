package cz.cvut.fit.sp.chipin.base.notification.content.mapper;

import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationContentMapper {
    NotificationReadNotificationsResponse entityToGetUserNotificationsResponse(NotificationContent notificationContent);
}

