package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountConverter;
import cz.cvut.fit.sp.chipin.base.group.Group;

import java.util.*;
import java.util.stream.Collectors;

public class TransactionConverter {
    public static TransactionResponse toDto(Transaction transaction) {
        return new TransactionResponse(transaction.getId(), transaction.getName(),
                Float.valueOf(String.format(Locale.getDefault(), "%.2f",
                        transaction.getAmount())), transaction.getDate(),
                new UserDTO(transaction.getPayer().getName()),
                transaction.getAmounts().stream().map(AmountConverter::toDto).collect(Collectors.toList()));
    }

    public static TransactionGroupResponse toTransactionGroupResponse(Transaction transaction) {
        return new TransactionGroupResponse(transaction.getId(), transaction.getName(),
                transaction.getAmount(), transaction.getDate(), transaction.getPayer().getName(),
                transaction.getAmounts().stream().map(Amount::getUserName).collect(Collectors.toList()));
    }

    public static List<TransactionGroupResponse> toGroupResponse(List<Transaction> transactions) {
        Collections.reverse(transactions);
        if (transactions.size() > 2) {
            return transactions.subList(0, 3).stream().map(TransactionConverter::toTransactionGroupResponse)
                    .collect(Collectors.toList());
        } else {
            return transactions.stream().map(TransactionConverter::toTransactionGroupResponse)
                    .collect(Collectors.toList());
        }
    }

    public static Transaction fromCreateDto(TransactionCreateRequest createRequest, User payer, Group group) {
        return new Transaction(createRequest.getName(), createRequest.getAmount(), payer, group);
    }
}
