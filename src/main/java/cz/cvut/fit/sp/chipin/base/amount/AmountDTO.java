package cz.cvut.fit.sp.chipin.base.amount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmountDTO {
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
}
