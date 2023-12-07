package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.usergroup.Group;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "debt")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Debt {

    @EmbeddedId
    private DebtKey id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "user_group_id", nullable = false)
    private Group group;

    @ManyToOne
    @MapsId("lenderId")
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @ManyToOne
    @MapsId("borrowerId")
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;

    public Debt(Group group, User lender, User borrower, Float amount) {
        id = new DebtKey(group.getId(), lender.getId(), borrower.getId());
        this.group = group;
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
    }
}
