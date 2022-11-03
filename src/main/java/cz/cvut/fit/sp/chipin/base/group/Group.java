package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.membership.Membership;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.log.Log;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "[group]")
@NoArgsConstructor
@Getter
@Setter
public class Group {
    @Id
    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )
    private Long id;

    @OneToMany(mappedBy = "group")
    private List<Debt> debts = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Log> logs = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Transaction> transactions = new ArrayList<>();

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public void addMembership(Membership membership) {
        memberships.add(membership);
    }
}
