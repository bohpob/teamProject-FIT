package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionReadGroupTransactionResponse {
    @NotBlank
    private String name;
    @NotBlank
    private Float amount;
    @NotBlank
    private String payerId;
    @NotBlank
    private String payerName;
    @NotBlank
    private String dateTime;
}
