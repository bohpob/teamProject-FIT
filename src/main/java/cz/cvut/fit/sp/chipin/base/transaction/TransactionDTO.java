package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
public class TransactionDTO {
    @NotNull
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
    @NotNull
    @NotBlank
    private String date;
    @NotNull
    private UserDTO payer;
    @NotNull
    private List<UserTransactionDTO> listOfUsers;
}
