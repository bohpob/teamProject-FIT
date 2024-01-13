package cz.cvut.fit.sp.chipin.base.notification.content.mapper;

import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationContentMapper {
    NotificationContentReadNotificationResponse entityToReadNotificationContentResponse(NotificationContent notificationContent);
}

