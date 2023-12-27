package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;
import cz.cvut.fit.sp.chipin.base.amount.AmountDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TransactionCreateTransactionResponse {
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
