package cz.cvut.fit.sp.chipin.base.amount;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionType;
import cz.cvut.fit.sp.chipin.base.amount.calculator.AmountSplitterProvider;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AmountService {
    private final AmountRepository amountRepository;
    private final UserService userService;

    public List<Amount> setAmounts(Transaction transaction, List<MemberAbstractRequest> spenders, TransactionType splitStrategy) throws Exception {
        if (spenders.isEmpty()) {
            throw new Exception("Users not found.");
        }

        List<User> users = new ArrayList<>();
        for (MemberAbstractRequest spender : spenders) {
            try {
                users.add(userService.getUser(spender.getSpenderId()));
            } catch (Exception e) {
                throw new Exception("User not found.");
            }
        }

        List<Amount> amounts = AmountSplitterProvider.getStrategy(splitStrategy).calculateAmounts(users, transaction, spenders);

        transaction.setAmounts(amounts);
        return amounts;
    }

    public void deleteAllByIds(List<AmountKey> ids) throws Exception {
        amountRepository.deleteAllById(ids);
    }

    public void saveAll(List<Amount> amounts) throws Exception {
        amountRepository.saveAll(amounts);
    }

    public void deleteAllByTransactionId(Long transactionId) throws Exception {
        amountRepository.deleteAmountsByTransactionId(transactionId);
    }

    public static Float roundAmount(Float amount) {
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
