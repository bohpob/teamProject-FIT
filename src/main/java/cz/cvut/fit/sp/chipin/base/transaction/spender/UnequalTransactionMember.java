package cz.cvut.fit.sp.chipin.base.transaction.spender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnequalTransactionMember extends MemberAbstractRequest {

    @NotNull
    private Float spent;

    public UnequalTransactionMember(@NotBlank String spenderId, Float spent) {
        super(spenderId);
        this.spent = spent;
    }
}
