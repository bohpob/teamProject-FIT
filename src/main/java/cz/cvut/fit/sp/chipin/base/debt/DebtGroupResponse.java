package cz.cvut.fit.sp.chipin.base.debt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
