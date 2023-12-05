package cz.cvut.fit.sp.chipin.base.debt.mapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DebtReadUserDebtsResponse {
    @NotBlank
    private String lenderId;
    @NotBlank
    private String lenderName;
    @NotBlank
    private String borrowerId;
    @NotBlank
    private String borrowerName;
    @NotNull
    private Float debt;
}
