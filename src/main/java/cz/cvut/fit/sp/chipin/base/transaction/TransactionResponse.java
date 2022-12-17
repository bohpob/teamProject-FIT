package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;
import cz.cvut.fit.sp.chipin.base.amount.AmountDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
public class TransactionResponse {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Float amount;
    @NotBlank
    private String date;
    @NotNull
    private UserDTO payer;
    @NotNull
    private List<AmountDTO> amounts;
}
