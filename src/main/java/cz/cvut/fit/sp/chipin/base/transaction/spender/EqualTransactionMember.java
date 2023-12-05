package cz.cvut.fit.sp.chipin.base.transaction.spender;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EqualTransactionMember extends MemberAbstractRequest {
    public EqualTransactionMember(@NotBlank String spenderId) {
        super(spenderId);
    }
}
