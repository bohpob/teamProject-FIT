package cz.cvut.fit.sp.chipin.base.balance;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Balance {

    @EmbeddedId
    private BalanceKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @NotBlank
    @Column
    private Float paid = 0f;

    @NotBlank
    @Column
    private Float spent = 0f;

    @NotBlank
    @Column
    private Float balance = 0f;
}
