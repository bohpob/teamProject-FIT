package cz.cvut.fit.sp.chipin.base.log;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;

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
    private Calendar date;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Log(String action, Calendar date, Group group, User user) {
        this.action = action;
        this.date = date;
        this.group = group;
        this.user = user;
    }
}