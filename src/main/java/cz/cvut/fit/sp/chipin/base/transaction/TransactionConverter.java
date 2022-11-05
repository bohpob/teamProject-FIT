package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.group.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionConverter {
    public static TransactionDetailDTO toDto(Transaction transaction) {
        return new TransactionDetailDTO(transaction.getId(), transaction.getName(), transaction.getAmount(), transaction.getDate(),
                new UserDTO(transaction.getPayer().getName()), getUsers(transaction.getAmounts(), transaction.getPayer().getId()));
    }

    private static List<UserTransactionDTO> getUsers(List<Amount> amounts, Long payer_id) {
        List<UserTransactionDTO> users = new ArrayList<>();
        for (Amount a : amounts) {
            if (!Objects.equals(a.getUser().getId(), payer_id)) {
                users.add(new UserTransactionDTO(a.getUser().getName(), a.getAmount()));
            }
        }
        return users;
    }

    public static Transaction fromDto(TransactionDTO transactionDTO, Group group, User payer) {
        return new Transaction(transactionDTO.getName(), transactionDTO.getAmount(), group, payer);
    }
}
