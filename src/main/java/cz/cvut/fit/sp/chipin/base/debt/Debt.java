package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "debt")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Debt {

    @EmbeddedId
    private DebtKey id;

    @ManyToOne()
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne()
    @JoinColumn(name = "lender_id")
    private User lender;

    @ManyToOne()
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @NotBlank
    @Column(name = "amount", nullable = false)
    private Float amount;
}
