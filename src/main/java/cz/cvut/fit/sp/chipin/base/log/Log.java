package cz.cvut.fit.sp.chipin.base.log;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "log")
@NoArgsConstructor
@Getter
@Setter
public class Log {
    @Id
    @SequenceGenerator(
            name = "log_sequence",
            sequenceName = "log_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "log_sequence"
    )
    private Long id;
    private String action;
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private User user;

    public Log(String action, String date, UserGroup userGroup, User user) {
        this.action = action;
        this.date = date;
        this.userGroup = userGroup;
        this.user = user;
    }
}