package cz.cvut.fit.sp.chipin.base.amount.calculator;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.spender.PercentTransactionMember;

import java.util.ArrayList;
import java.util.List;

public class PercentBasedAmountCalculator implements AmountCalculator {
    @Override
    public List<Amount> calculateAmounts(List<User> users, Transaction transaction, List<MemberAbstractRequest> spenders) throws Exception {
        List<Amount> amounts = new ArrayList<>();

        float totalPercent = 0f;
        for (int i = 0; i < users.size(); i++) {
            try {
                PercentTransactionMember memberByPercentages = (PercentTransactionMember) spenders.get(i);
                float percent = memberByPercentages.getPercentage();
                amounts.add(new Amount(users.get(i), transaction, percent * transaction.getAmount() / 100));
                totalPercent += percent;
            } catch (Exception e) {
                throw new Exception("User not found.");
            }
        }

        if (Math.abs(totalPercent - 100) > 0.01)
            throw new Exception("The total percentage is not 100");
        return amounts;
    }
}
