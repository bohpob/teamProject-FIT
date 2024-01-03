package cz.cvut.fit.sp.chipin.base.notification.content;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_content")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationContent {
    @Id
    @SequenceGenerator(
            name = "notification_content_sequence",
            sequenceName = "notification_content_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_content_sequence"
    )
    private Long id;

    @NotBlank
    private String title;

    private String text;
}
