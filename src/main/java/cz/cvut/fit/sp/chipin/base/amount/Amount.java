package cz.cvut.fit.sp.chipin.base.amount;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "amount")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Amount {
    @EmbeddedId
    private AmountKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

    @ManyToOne
    @MapsId("transactionId")
    @JoinColumn(name = "transaction_id", nullable=false)
    private Transaction transaction;

    @NotNull
    @Column
    private Float amount = 0f;

    public Amount(User user, Transaction transaction, Float amount) {
        id = new AmountKey(user.getId(), transaction.getId());
        this.user = user;
        this.transaction = transaction;
        this.amount = amount;
    }
}
