package cz.cvut.fit.sp.chipin.base.debt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class DebtKeyDTO {
    @NotNull
    private Long lenderId;

    @NotNull
    private Long borrowerId;
}
