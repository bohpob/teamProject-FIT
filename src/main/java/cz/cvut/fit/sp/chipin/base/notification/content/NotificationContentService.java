package cz.cvut.fit.sp.chipin.base.notification.content;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationContentService {
    private final NotificationContentRepository notificationContentRepository;

    public NotificationContent readNotificationContent(Long id) throws Exception {
        return notificationContentRepository.findById(id).orElseThrow(() -> new Exception("Notification content not found."));
    }

    public void createNotificationContent(NotificationContent notificationContent) {
        notificationContentRepository.save(notificationContent);
    }
}
