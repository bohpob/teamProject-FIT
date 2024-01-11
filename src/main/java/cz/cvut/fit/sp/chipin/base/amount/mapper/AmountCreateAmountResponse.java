package cz.cvut.fit.sp.chipin.base.amount.mapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AmountCreateAmountResponse {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
}
