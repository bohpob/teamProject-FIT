package cz.cvut.fit.sp.chipin.base.amount;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @MapsId("userAccountId")
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @ManyToOne
    @MapsId("transactionId")
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @NotNull
    @Column
    private Float amount = 0f;

    public Amount(UserAccount userAccount, Transaction transaction, Float amount) {
        id = new AmountKey(userAccount.getId(), transaction.getId());
        this.userAccount = userAccount;
        this.transaction = transaction;
        this.amount = amount;
    }

    public Float reverse() {
        return amount * -1;
    }

    public String getUserName() {
        return userAccount.getName();
    }

}
