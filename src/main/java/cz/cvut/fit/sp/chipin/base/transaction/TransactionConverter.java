package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.group.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionConverter {
    public static TransactionDTO toDto(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getName(),
                Float.valueOf(String.format(Locale.getDefault(), "%.2f", transaction.getAmount())), transaction.getDate(),
                new UserDTO(transaction.getPayer().getName()), getUsers(transaction.getAmounts()));
    }

    private static List<UserTransactionDTO> getUsers(List<Amount> amounts) {
        List<UserTransactionDTO> users = new ArrayList<>();
        for (Amount a : amounts) {
            users.add(new UserTransactionDTO(a.getUser().getName(),
                    Float.valueOf(String.format(Locale.getDefault(), "%.2f", a.getAmount()))));
        }
        return users;
    }

    public static Transaction fromDto(TransactionCreateRequest transactionDTO, Group group, User payer) {
        return new Transaction(transactionDTO.getName(), transactionDTO.getAmount(), group, payer);
    }
}
