package cz.cvut.fit.sp.chipin.base.amount;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.authentication.user.UserService;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AmountService {
    private final AmountRepository amountRepository;
    private final UserService userService;

    public List<Amount> setAmounts(List<Long> spenderIds, Transaction transaction) throws Exception {
        if (spenderIds.isEmpty()) {
            throw new Exception("Users not found.");
        }

        List<Amount> amounts = new ArrayList<>();
        Float spent = transaction.getAmount() / spenderIds.size();

        for (Long id : spenderIds) {
            try {
                User user = userService.getUser(id);
                amounts.add(new Amount(user, transaction, spent));
            } catch (Exception e) {
                throw new Exception("User not found.");
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
