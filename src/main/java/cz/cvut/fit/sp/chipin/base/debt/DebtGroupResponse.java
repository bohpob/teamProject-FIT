package cz.cvut.fit.sp.chipin.base.debt;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class DebtGroupResponse {
    @NotBlank
    private final String lender;
    @NotBlank
    private final String borrower;
    @NotNull
    private final Float amount;
}
