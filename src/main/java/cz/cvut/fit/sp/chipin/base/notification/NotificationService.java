package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContentService;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationGetUserNotificationsResponse;
import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationMapper;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationContentService notificationContentService;

    public Page<NotificationGetUserNotificationsResponse> getUserNotifications(String username, Pageable pageable) {
        try {
            Page<Notification> page = notificationRepository.findNotificationByUserId(username, pageable);
            return page.map(notificationMapper::entityToGetUserNotificationsResponse);
        } catch (Exception e) {
            return Page.empty();
        }
    }

    public void markNotificationAsRead(Long notificationId, String username) throws Exception {
         Notification notification = notificationRepository.findByIdAndUserId(notificationId, username)
                 .orElseThrow(() -> new Exception("Group not found"));
         notification.setRead(true);
         notificationRepository.save(notification);
    }

    public void markNotificationAsUnread(Long notificationId, String username) throws Exception {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, username)
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
        notificationToUpdate.setTimestamp(notification.getTimestamp());
        notificationToUpdate.setTransaction(notification.getTransaction());
        notificationRepository.save(notificationToUpdate);
    }

    public void createNotification(User user, Group group, Transaction transaction, NotificationContent content) {
        notificationContentService.createNotificationContent(content);
        Notification notification = new Notification(content, user, group, transaction);
        notificationRepository.save(notification);
    }
    public void createNotifications(List<User> users, Group group, Transaction transaction, NotificationContent content) {
        notificationContentService.createNotificationContent(content);
        for (User user : users) {
            Notification notification = new Notification(content, user, group, transaction);
            notificationRepository.save(notification);
        }
    }
}
