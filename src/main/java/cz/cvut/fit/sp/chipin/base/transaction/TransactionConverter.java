package cz.cvut.fit.sp.chipin.base.transaction;

import cz.cvut.fit.sp.chipin.authentication.user.UserDTO;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.amount.AmountConverter;
import cz.cvut.fit.sp.chipin.base.amount.AmountDTO;
import cz.cvut.fit.sp.chipin.base.membership.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionConverter {
    public static TransactionDTO toDto(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getName(),
                Float.valueOf(String.format(Locale.getDefault(), "%.2f", transaction.getAmount())), transaction.getDate(),
                new UserDTO(transaction.getPayer().getUser().getName()), getAmounts(transaction.getAmounts()));
    }

    private static List<AmountDTO> getAmounts(List<Amount> amounts) {
        List<AmountDTO> result = new ArrayList<>();
        for (Amount a : amounts) {
            result.add(AmountConverter.toDto(a));
        }
        return result;
    }

    public static Transaction fromCreateDto(TransactionCreateRequest transactionCreateRequest, Member payer) {
        return new Transaction(transactionCreateRequest.getName(), transactionCreateRequest.getAmount(), payer);
    }

    public static Transaction fromUpdateDto(TransactionUpdateRequest transactionUpdateRequest, Member payer) {
        return new Transaction(transactionUpdateRequest.getName(), transactionUpdateRequest.getAmount(), transactionUpdateRequest.getDate(), payer);
    }
}
