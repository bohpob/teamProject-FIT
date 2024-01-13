package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.base.notification.mapper.NotificationReadNotificationsResponse;
import cz.cvut.fit.sp.chipin.base.notification.swagger.NotificationSwaggerExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
@Tag(name = "Notification Controller")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get all user notifications", description = "Returns all user notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = NotificationReadNotificationsResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = NotificationSwaggerExamples.EXAMPLE_ALL_NOTIFICATION_JSON))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})

    // Returns all user notifications
    @GetMapping("/me/all")
    public ResponseEntity<Page<NotificationReadNotificationsResponse>> readAllNotifications(
            @Parameter(hidden = true) Pageable pageable, Principal principal) {
        Page<NotificationReadNotificationsResponse> notifications =
                notificationService.readAllNotifications(principal.getName(), pageable);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Get all unread user notifications", description = "Returns only unread user notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = NotificationReadNotificationsResponse.class),
                            mediaType = "application/json", examples = @ExampleObject(
                            value = NotificationSwaggerExamples.EXAMPLE_UNREAD_NOTIFICATION_JSON))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})

    // Returns only unread user notifications
    @GetMapping("/me/unread")
    public ResponseEntity<Page<NotificationReadNotificationsResponse>> readUnreadNotifications(
            @Parameter(hidden = true) Pageable pageable, Principal principal) {
        Page<NotificationReadNotificationsResponse> notifications =
                notificationService.readUnreadNotifications(principal.getName(), pageable);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Reverses the notification status to the opposite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)})

    // Reverses the notification status to the opposite
    @PatchMapping("/me/{notificationId}")
    public ResponseEntity<Void> reverseNotificationStatus(
            @Parameter(description = "Notification ID") @PathVariable Long notificationId, Principal principal) {
        try {
            notificationService.reverseNotificationStatus(notificationId, principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
