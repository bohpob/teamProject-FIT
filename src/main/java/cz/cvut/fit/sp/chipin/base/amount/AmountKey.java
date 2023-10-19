package cz.cvut.fit.sp.chipin.base.amount;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
