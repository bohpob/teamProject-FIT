package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class DebtDTO {
    @NotNull
    private final Long id;
    @NotNull
    private final UserDTO lender;
    @NotNull
    private final UserDTO borrower;
    @NotNull
    private final Integer amount;
}
