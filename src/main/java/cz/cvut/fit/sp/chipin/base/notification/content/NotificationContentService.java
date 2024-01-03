package cz.cvut.fit.sp.chipin.base.notification.content;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationContentService {
    private final NotificationContentRepository notificationContentRepository;

    public NotificationContent getNotificationContent(Long id) throws Exception {
        return notificationContentRepository.findById(id).orElseThrow(() -> new Exception("Notification content not found."));
    }
    public void createNotificationContent(NotificationContent notificationContent) {
        notificationContentRepository.save(notificationContent);
    }

    public void deleteNotificationContent(Long id) throws Exception {
        NotificationContent notificationContent = notificationContentRepository.findById(id).orElseThrow(() -> new Exception("Notification content not found."));
        notificationContentRepository.delete(notificationContent);
    }

    public void updateNotificationContent(NotificationContent notificationContent) throws Exception {
        NotificationContent notificationContentToUpdate = notificationContentRepository.findById(notificationContent.getId()).orElseThrow(() -> new Exception("Notification content not found."));
        notificationContentToUpdate.setText(notificationContent.getText());
        notificationContentToUpdate.setTitle(notificationContent.getTitle());
        notificationContentRepository.save(notificationContentToUpdate);
    }
}
