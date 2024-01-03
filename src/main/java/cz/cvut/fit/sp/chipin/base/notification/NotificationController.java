package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationListWithCountResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // Returns all user notifications
    @GetMapping()
    public ResponseEntity<NotificationListWithCountResponse> readNotifications(Principal principal) {
        NotificationListWithCountResponse notifications = notificationService.readNotifications(principal.getName());
        return ResponseEntity.ok(notifications);
    }

    // (Un)read notification
    @PatchMapping("/{notificationId}")
    public ResponseEntity<Void> readNotification(@PathVariable Long notificationId, @RequestBody Boolean newStatus,
                                                 Principal principal) {
        try {
            notificationService.markNotification(notificationId, principal.getName(), newStatus);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
