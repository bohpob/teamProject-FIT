package cz.cvut.fit.sp.chipin.base.transaction.spender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShareTransactionMember extends MemberAbstractRequest {
    @NotNull
    private Float share;

    public ShareTransactionMember(@NotBlank String spenderId, Float share) {
        super(spenderId);
        this.share = share;
    }
}
