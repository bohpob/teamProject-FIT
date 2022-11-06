package cz.cvut.fit.sp.chipin.base.amount;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AmountKey implements Serializable {
    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "transaction_id")
    private Long transactionId;
}
