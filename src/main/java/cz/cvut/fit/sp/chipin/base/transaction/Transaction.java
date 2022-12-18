package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.membership.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private String date;
    @ManyToOne
    @JoinColumns(foreignKey = @ForeignKey(name = "MemberKey"), value = {
            @JoinColumn(referencedColumnName = "user_id"),
            @JoinColumn(referencedColumnName = "group_id")
    })
    private Member payer;

    @OneToMany(mappedBy = "transaction")
    private List<Amount> amounts = new ArrayList<>();

    public Transaction(String name, Float amount, Member payer) {
        this.name = name;
        this.amount = amount;
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm");
        this.date = currentDate.format(formatter);
        this.payer = payer;
    }

}