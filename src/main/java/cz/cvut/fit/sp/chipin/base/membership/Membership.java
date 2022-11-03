package cz.cvut.fit.sp.chipin.base.membership;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "membership")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Membership {

    @EmbeddedId
    private MembershipKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable=false)
    private Group group;

    @NotNull
    @Column
    private GroupRole role;

    @NotNull
    @Column
    private Float paid = 0f;

    @NotNull
    @Column
    private Float spent = 0f;

    @NotNull
    @Column
    private Float balance = 0f;

    public Membership(User user, Group group, GroupRole role, Float paid, Float spent, Float balance) {
        id = new MembershipKey(user.getId(), group.getId());
//        id.setUserId(user.getId());
//        id.setGroupId(group.getId());
        this.user = user;
        this.group = group;
        this.role = role;
        this.paid = paid;
        this.spent = spent;
        this.balance = balance;
    }
}
