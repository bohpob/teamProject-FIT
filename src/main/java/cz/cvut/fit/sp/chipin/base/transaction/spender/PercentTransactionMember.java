package cz.cvut.fit.sp.chipin.base.transaction.spender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PercentTransactionMember extends MemberAbstractRequest {
    @NotNull
    private Float percentage;

    public PercentTransactionMember(@NotBlank String spenderId, Float percentage) {
        super(spenderId);
        this.percentage = percentage;
    }
}
