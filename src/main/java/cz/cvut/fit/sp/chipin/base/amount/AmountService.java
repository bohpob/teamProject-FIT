package cz.cvut.fit.sp.chipin.base.amount;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccountService;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.TransactionType;
import cz.cvut.fit.sp.chipin.base.amount.calculator.AmountSplitterProvider;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AmountService {
    private final AmountRepository amountRepository;
    private final UserAccountService userAccountService;

    public List<Amount> setAmounts(Transaction transaction, List<MemberAbstractRequest> spenders, TransactionType splitStrategy) throws Exception {
        if (spenders.isEmpty()) {
            throw new Exception("UserAccounts not found.");
        }

        List<UserAccount> userAccounts = new ArrayList<>();
        for (MemberAbstractRequest spender : spenders) {
            try {
                userAccounts.add(userAccountService.getUserAccount(spender.getSpenderId()));
            } catch (Exception e) {
                throw new Exception("UserAccount not found.");
            }
        }

        List<Amount> amounts = AmountSplitterProvider.getStrategy(splitStrategy).calculateAmounts(userAccounts, transaction, spenders);

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
}
