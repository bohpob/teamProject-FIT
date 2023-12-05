package cz.cvut.fit.sp.chipin.base.transaction.spender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdjustmentTransactionMember extends MemberAbstractRequest {
    @NotNull
    private Float adjustment;

    public AdjustmentTransactionMember(@NotBlank String spenderId, Float adjustment) {
        super(spenderId);
        this.adjustment = adjustment;
    }
}
