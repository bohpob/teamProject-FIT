package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContentService;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationResponse;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationsResponse;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationContentService notificationContentService;

    public NotificationReadNotificationsResponse readNotifications(String userId) {
        List<Notification> notifications = notificationRepository.findNotificationsByUserId(userId);

        Long count = notifications.stream().filter(notification -> Boolean.FALSE.equals(notification.getRead())).count();

        List<NotificationReadNotificationResponse> notificationResponses = notifications.stream()
                .map(notificationMapper::entityToReadNotificationResponse)
                .toList();

        return new NotificationReadNotificationsResponse(count, notificationResponses);
    }

    public void reverseNotificationStatus(Long notificationId, String userId) throws Exception {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new Exception("Group not found"));

        notification.setRead(!notification.getRead());
        notificationRepository.save(notification);
    }

    public void createNotification(User user, Group group, NotificationContent content) {
        notificationContentService.createNotificationContent(content);
        Notification notification = new Notification(content, user, group);
        notificationRepository.save(notification);
    }

    public void createNotifications(List<User> users, Group group, NotificationContent content) {
        notificationContentService.createNotificationContent(content);
        for (User user : users) {
            Notification notification = new Notification(content, user, group);
            notificationRepository.save(notification);
        }
    }
}
