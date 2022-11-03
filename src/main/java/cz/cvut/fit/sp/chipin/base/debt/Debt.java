package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @JoinColumn(name = "group_id", nullable=false)
    Group group;

    @ManyToOne
    @MapsId("lenderId")
    @JoinColumn(name = "lender_id", nullable=false)
    User lender;

    @ManyToOne
    @MapsId("borrowerId")
    @JoinColumn(name = "borrower_id", nullable=false)
    User borrower;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;
}
