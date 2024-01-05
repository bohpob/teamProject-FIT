package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationsResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/me/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // Returns all user notifications
    @GetMapping()
    public ResponseEntity<NotificationReadNotificationsResponse> readNotifications(Principal principal) {
        NotificationReadNotificationsResponse notifications = notificationService.readNotifications(principal.getName());
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
