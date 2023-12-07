package cz.cvut.fit.sp.chipin.base.transaction.spender;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberAbstractRequest {
    @NotBlank
    private String spenderId;
}
