package cz.cvut.fit.sp.chipin.base.group.mapper;

import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GroupReadGroupTransactionsResponse {
    @NotNull
    List<TransactionReadGroupTransactionsResponse> transactions;
}
