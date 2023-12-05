package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionReadGroupTransactionsResponse {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private Float amount;
    @NotBlank
    private String payer;
    @NotBlank
    private String date;
}
