package cz.cvut.fit.sp.chipin.authentication.user.mapper;

import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserReadUserTransactionsResponse {
    @NotNull
    private final List<TransactionReadGroupTransactionsResponse> transactions;
}
