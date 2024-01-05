package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import cz.cvut.fit.sp.chipin.base.amount.mapper.AmountCreateAmountResponse;
import cz.cvut.fit.sp.chipin.base.transaction.Category;
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
    @NotNull
    private String category;
    @NotBlank
    private String dateTime;
    @NotNull
    private UserCreateTransactionResponse payer;
    @NotNull
    private List<AmountCreateAmountResponse> amounts;
}
