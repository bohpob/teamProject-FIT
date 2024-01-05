package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationsResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/me/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // Returns all user notifications
    @GetMapping("/all")
    public ResponseEntity<Page<NotificationReadNotificationsResponse>> readAllNotifications(Pageable pageable, Principal principal) {
        Page<NotificationReadNotificationsResponse> notifications =
                notificationService.readAllNotifications(principal.getName(), pageable);
        return ResponseEntity.ok(notifications);
    }

    // Returns only unread user notifications
    @GetMapping("/unread")
    public ResponseEntity<Page<NotificationReadNotificationsResponse>> readUnreadNotifications(Pageable pageable, Principal principal) {
        Page<NotificationReadNotificationsResponse> notifications =
                notificationService.readUnreadNotifications(principal.getName(), pageable);
        return ResponseEntity.ok(notifications);
    }

    // Reverses the notification status to the opposite
    @PatchMapping("/{notificationId}")
    public ResponseEntity<Void> reverseNotificationStatus(@PathVariable Long notificationId, Principal principal) {
        try {
            notificationService.reverseNotificationStatus(notificationId, principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
