package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionReadGroupTransactionsResponse {

    @NotBlank
    private String name;
//    @NotBlank
//    private String currency;
    @NotBlank
    private Float amount;
    @NotBlank
    private String payer;
    @NotBlank
    private String date;
}
