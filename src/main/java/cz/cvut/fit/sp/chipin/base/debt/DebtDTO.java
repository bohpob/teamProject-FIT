package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;

import lombok.*;

@Getter
@AllArgsConstructor
public class DebtDTO {
    private final Long id;
    private final UserDTO lender;
    private final UserDTO borrower;
    private final Integer amount;
}
