package cz.cvut.fit.sp.chipin.base.amount.calculator;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;

import java.util.ArrayList;
import java.util.List;

public class EqualAmountCalculator implements AmountCalculator {
    @Override
    public List<Amount> calculateAmounts(List<User> users, Transaction transaction, List<MemberAbstractRequest> spenders) throws Exception {
        List<Amount> amounts = new ArrayList<>();
        Float spent = transaction.getConvertedAmount() / users.size();

        for (User user : users) {
            try {
                amounts.add(new Amount(user, transaction, spent));
            } catch (Exception e) {
                throw new Exception("User not found.");
            }
        }
        return amounts;
    }
}
