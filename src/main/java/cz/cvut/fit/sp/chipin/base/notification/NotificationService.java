package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContentService;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationsResponse;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationListWithCountResponse;
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

    public NotificationListWithCountResponse readNotifications(String userId) {
        List<Notification> notifications = notificationRepository.findNotificationsByUserId(userId);

        Long count = notifications.stream().filter(notification -> Boolean.FALSE.equals(notification.getRead())).count();

        List<NotificationReadNotificationsResponse> notificationResponses = notifications.stream()
                .map(notificationMapper::entityToGetUserNotificationsResponse)
                .toList();

        return new NotificationListWithCountResponse(count, notificationResponses);
    }

    public void markNotificationAsRead(Long notificationId, String userId) throws Exception {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new Exception("Group not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markNotificationAsUnread(Long notificationId, String userId) throws Exception {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new Exception("Notification not found."));
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) throws Exception {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new Exception("Notification not found."));
        notificationRepository.delete(notification);
    }

    public void updateNotification(Notification notification) throws Exception {
        Notification notificationToUpdate = notificationRepository.findById(notification.getId()).orElseThrow(() -> new Exception("Notification not found."));
        notificationToUpdate.setContent(notification.getContent());
        notificationToUpdate.setGroupName(notification.getGroupName());
        notificationToUpdate.setUser(notification.getUser());
        notificationToUpdate.setRead(notification.getRead());
        notificationToUpdate.setDateTime(notification.getDateTime());
        notificationRepository.save(notificationToUpdate);
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
