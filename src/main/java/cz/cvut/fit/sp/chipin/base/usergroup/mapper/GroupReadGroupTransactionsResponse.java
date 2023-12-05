package cz.cvut.fit.sp.chipin.base.usergroup.mapper;

import cz.cvut.fit.sp.chipin.base.transaction.mapper.TransactionReadGroupTransactionsResponse;
import lombok.Data;

import java.util.List;

@Data
public class GroupReadGroupTransactionsResponse {
    List<TransactionReadGroupTransactionsResponse> transactions;
}
