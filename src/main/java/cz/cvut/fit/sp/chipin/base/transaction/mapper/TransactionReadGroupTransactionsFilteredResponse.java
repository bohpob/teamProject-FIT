package cz.cvut.fit.sp.chipin.base.transaction.mapper;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TransactionReadGroupTransactionsFilteredResponse {
    @NotNull
    List<TransactionReadGroupTransactionsResponse> transactions;
}
