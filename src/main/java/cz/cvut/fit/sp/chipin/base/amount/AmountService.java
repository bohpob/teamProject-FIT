package cz.cvut.fit.sp.chipin.base.amount;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccountService;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AmountService {
    private final AmountRepository amountRepository;
    private final UserAccountService userAccountService;

    public List<Amount> setAmounts(List<String> spenderIds, Transaction transaction) throws Exception {
        if (spenderIds.isEmpty()) {
            throw new Exception("UserAccounts not found.");
        }

        List<Amount> amounts = new ArrayList<>();
        Float spent = transaction.getAmount() / spenderIds.size();

        for (String id : spenderIds) {
            try {
                UserAccount userAccount = userAccountService.getUserAccount(id);
                amounts.add(new Amount(userAccount, transaction, spent));
            } catch (Exception e) {
                throw new Exception("UserAccount not found.");
            }
        }

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
