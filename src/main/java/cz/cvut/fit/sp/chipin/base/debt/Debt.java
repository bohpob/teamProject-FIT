package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.usergroup.UserGroup;
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
    @MapsId("userGroupId")
    @JoinColumn(name = "user_group_id", nullable = false)
    private UserGroup userGroup;

    @ManyToOne
    @MapsId("lenderId")
    @JoinColumn(name = "lender_id", nullable = false)
    private UserAccount lender;

    @ManyToOne
    @MapsId("borrowerId")
    @JoinColumn(name = "borrower_id", nullable = false)
    private UserAccount borrower;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;

    public Debt(UserGroup userGroup, UserAccount lender, UserAccount borrower, Float amount) {
        id = new DebtKey(userGroup.getId(), lender.getId(), borrower.getId());
        this.userGroup = userGroup;
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
    }
}
