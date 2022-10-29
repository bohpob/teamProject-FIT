package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.group.Group;

import java.util.ArrayList;
import java.util.List;

public class TransactionConverter {
    public static TransactionDTO toDto(Transaction transaction) {
        return new TransactionDTO(transaction.getName(), transaction.getAmount(), transaction.getDate(),
                transaction.getPayer().getName(), transaction.getPayer().getId(), getUserIds(transaction.getAmounts()));
    }

    private static List<Long> getUserIds(List<Amount> amounts) {
        List<Long> userIds = new ArrayList<>();
        for (Amount a : amounts) {
            userIds.add(a.getUser().getId());
        }
        return userIds;
    }

    public static Transaction fromDto(TransactionDTO transactionDTO, Group group, User payer) {
        return new Transaction(transactionDTO.getName(), transactionDTO.getAmount(), transactionDTO.getDate(), group, payer);
    }
}
