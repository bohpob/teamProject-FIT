package cz.cvut.fit.sp.chipin.base.notification.mapper;

import cz.cvut.fit.sp.chipin.base.notification.Notification;
import cz.cvut.fit.sp.chipin.base.notification.content.mapper.NotificationContentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring", uses = {NotificationContentMapper.class})
public interface NotificationMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "transaction.id", target = "transactionId")
    NotificationGetUserNotificationsResponse entityToGetUserNotificationsResponse(Notification notification);

}

