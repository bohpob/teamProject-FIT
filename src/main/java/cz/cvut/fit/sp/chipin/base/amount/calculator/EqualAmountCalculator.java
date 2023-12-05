package cz.cvut.fit.sp.chipin.base.amount.calculator;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;

import java.util.ArrayList;
import java.util.List;

public class EqualAmountCalculator implements AmountCalculator {
    @Override
    public List<Amount> calculateAmounts(List<UserAccount> users, Transaction transaction, List<MemberAbstractRequest> spenders) throws Exception {
        List<Amount> amounts = new ArrayList<>();
        Float spent = transaction.getAmount() / users.size();

        for (UserAccount user : users) {
            try {
                amounts.add(new Amount(user, transaction, spent));
            } catch (Exception e) {
                throw new Exception("UserAccount not found.");
            }
        }
        return amounts;
    }
}
