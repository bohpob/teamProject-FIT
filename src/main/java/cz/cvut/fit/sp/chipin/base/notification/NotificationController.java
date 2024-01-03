package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationGetUserNotificationsResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // Get user notifications with pagination
    @GetMapping("/user")
    public ResponseEntity<Page<NotificationGetUserNotificationsResponse>> getUserNotifications(
            Pageable pageable,
            Principal principal) {
        Page<NotificationGetUserNotificationsResponse> notifications = notificationService.getUserNotifications(principal.getName(), pageable);
        return ResponseEntity.ok(notifications);
    }

    // Read notification
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> readNotification(
            @PathVariable Long notificationId,
            Principal principal) {
        try {
            notificationService.markNotificationAsRead(notificationId, principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Unread notification
    @DeleteMapping("/{notificationId}/read")
    public ResponseEntity<Void> unreadNotification(
            @PathVariable Long notificationId,
            Principal principal) {
        try {
            notificationService.markNotificationAsUnread(notificationId, principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
