package cz.cvut.fit.sp.chipin.base.amount;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.group.Group;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("transactionId")
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @NotBlank
    @Column
    private Float amount = 0f;

    public Amount(User user, Transaction transaction, Float amount) {
        this.user = user;
        this.transaction = transaction;
        this.amount = amount;
    }
}
