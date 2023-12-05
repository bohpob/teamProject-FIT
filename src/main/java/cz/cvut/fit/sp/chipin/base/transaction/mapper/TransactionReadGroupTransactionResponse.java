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
    private String payer;
    @NotBlank
    private String date;
}
