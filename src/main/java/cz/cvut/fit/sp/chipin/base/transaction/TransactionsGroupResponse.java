package cz.cvut.fit.sp.chipin.base.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TransactionsGroupResponse {

    private List<TransactionGroupResponse> transactions;

}
