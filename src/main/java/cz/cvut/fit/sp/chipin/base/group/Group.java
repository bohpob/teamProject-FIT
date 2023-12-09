package cz.cvut.fit.sp.chipin.base.group;

import cz.cvut.fit.sp.chipin.base.member.Member;
import cz.cvut.fit.sp.chipin.base.debt.Debt;
import cz.cvut.fit.sp.chipin.base.log.Log;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_group")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Group {
    @Id
    @SequenceGenerator(
            name = "user_group_sequence",
            sequenceName = "user_group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_group_sequence"
    )
    private Long id;

    @OneToMany(mappedBy = "group")
    private List<Debt> debts = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Log> logs = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Transaction> transactions = new ArrayList<>();

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotBlank
    private String hexCode;

    public Group(String name, Currency currency, String hexCode) {
        this.name = name;
        this.currency = currency;
        this.hexCode = hexCode;
    }

    public void addMembership(Member member) {
        members.add(member);
    }
}
