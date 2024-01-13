package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContentService;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationResponse;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationsResponse;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationContentService notificationContentService;

    public Page<NotificationReadNotificationsResponse> readAllNotifications(String userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findNotificationsByUserId(userId, pageable);

        List<NotificationReadNotificationResponse> notificationResponses = notifications.stream()
                .map(notificationMapper::entityToReadNotificationResponse)
                .sorted(Comparator.comparing(NotificationReadNotificationResponse::getId).reversed())
                .toList();

        NotificationReadNotificationsResponse response = new NotificationReadNotificationsResponse(
                notifications.getTotalElements(), notificationResponses);
        return new PageImpl<>(Collections.singletonList(response), pageable, notifications.getTotalElements());
    }

    public Page<NotificationReadNotificationsResponse> readUnreadNotifications(String userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findNotificationsByUserId(userId, pageable);

        long count = notifications.stream().filter(notification -> Boolean.FALSE.equals(notification.getRead())).count();

        List<NotificationReadNotificationResponse> unreadNotificationResponses = notifications.stream()
                .filter(notification -> Boolean.FALSE.equals(notification.getRead()))
                .map(notificationMapper::entityToReadNotificationResponse)
                .sorted(Comparator.comparing(NotificationReadNotificationResponse::getId).reversed())
                .toList();

        NotificationReadNotificationsResponse response = new NotificationReadNotificationsResponse(count, unreadNotificationResponses);
        return new PageImpl<>(Collections.singletonList(response), pageable, count);
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
