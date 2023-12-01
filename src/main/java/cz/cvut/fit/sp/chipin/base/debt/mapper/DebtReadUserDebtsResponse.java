package cz.cvut.fit.sp.chipin.base.debt.mapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DebtReadUserDebtsResponse {
    @NotBlank
    private String lender;
    @NotBlank
    private String borrower;
    @NotNull
    private Float debt;
}
