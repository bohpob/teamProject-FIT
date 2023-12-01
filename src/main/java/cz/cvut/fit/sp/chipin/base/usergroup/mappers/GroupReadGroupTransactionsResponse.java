package cz.cvut.fit.sp.chipin.base.usergroup.mappers;

import cz.cvut.fit.sp.chipin.base.transaction.mappers.TransactionReadGroupTransactionsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class GroupReadGroupTransactionsResponse {
    List<TransactionReadGroupTransactionsResponse> transactions;
}
