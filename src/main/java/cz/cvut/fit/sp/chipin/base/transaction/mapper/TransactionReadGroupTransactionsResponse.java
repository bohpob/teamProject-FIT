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
    private String currency;
    @NotBlank
    private String payerId;
    @NotBlank
    private String payerName;
    @NotBlank
    private String category;
    @NotBlank
    private String dateTime;
}
