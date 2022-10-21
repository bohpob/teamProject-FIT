package cz.cvut.fit.sp.chipin.base.debt;

import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;

import lombok.*;

@Getter
@AllArgsConstructor
public class DebtDTO {
    private final Integer id;
    private UserDTO lender;
    private UserDTO borrower;
    private Integer amount;
}
