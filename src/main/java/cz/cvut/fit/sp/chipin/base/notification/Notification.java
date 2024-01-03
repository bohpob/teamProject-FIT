package cz.cvut.fit.sp.chipin.base.notification;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.notification.content.NotificationContent;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {
    @Id
    @SequenceGenerator(
            name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_sequence"
    )
    private Long id;

    @NotNull
    private Long timestamp;

    @NotNull
    private Boolean read;

    private String groupName;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "notification_content_id")
    private NotificationContent content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    // simple constructor with generated timestamp and read set to false
    public Notification(NotificationContent content, User user, Group group, Transaction transaction) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
        this.read = false;
        this.user = user;
        this.groupName = group.getName();
        this.transaction = transaction;
    }
}
