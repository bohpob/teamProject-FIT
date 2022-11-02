package cz.cvut.fit.sp.chipin.base.membership;

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

    @NotBlank
    @Column
    private GroupRole role;

    @NotBlank
    @Column
    private Float paid = 0f;

    @NotBlank
    @Column
    private Float spent = 0f;

    @NotBlank
    @Column
    private Float balance = 0f;

    public Membership(User user, Group group, GroupRole role, Float paid, Float spent, Float balance) {
        this.user = user;
        this.group = group;
        this.role = role;
        this.paid = paid;
        this.spent = spent;
        this.balance = balance;
    }
}
