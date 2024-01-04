package cz.cvut.fit.sp.chipin.base.notification.mapper;

import cz.cvut.fit.sp.chipin.base.notification.Notification;
import cz.cvut.fit.sp.chipin.base.notification.content.mapper.NotificationContentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {NotificationContentMapper.class})
public interface NotificationMapper {
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    @Mapping(target = "dateTime", dateFormat = DATETIME_FORMAT)
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "transaction.id", target = "transactionId")
    NotificationReadNotificationResponse entityToReadNotificationResponse(Notification notification);

}

