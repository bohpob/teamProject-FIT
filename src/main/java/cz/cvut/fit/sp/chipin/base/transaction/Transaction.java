package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.group.Group;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @SequenceGenerator(
            name = "transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_sequence"
    )
    private Long id;
    private String name;
    private Float amount;
    private Calendar date;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User payer;

    @OneToMany(mappedBy = "transaction")
    private List<Amount> amounts = new ArrayList<>();

    public Transaction(String name, Float amount, Calendar date, Group group, User payer) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.group = group;
        this.payer = payer;
    }

    public void addAmount(Amount amount) {
        amounts.add(amount);
    }
}