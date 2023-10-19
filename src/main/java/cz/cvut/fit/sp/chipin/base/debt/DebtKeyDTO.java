package cz.cvut.fit.sp.chipin.base.debt;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DebtKeyDTO {
    @NotNull
    private Long lenderId;

    @NotNull
    private Long borrowerId;
}
