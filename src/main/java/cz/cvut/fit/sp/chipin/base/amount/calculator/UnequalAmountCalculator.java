package cz.cvut.fit.sp.chipin.base.amount.calculator;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.spender.UnequalTransactionMember;

import java.util.ArrayList;
import java.util.List;

public class UnequalAmountCalculator implements AmountCalculator {
    @Override
    public List<Amount> calculateAmounts(List<User> users, Transaction transaction, List<MemberAbstractRequest> spenders) throws Exception {
        List<Amount> amounts = new ArrayList<>();

        float totalSpent = 0f;
        for (int i = 0; i < users.size(); i++) {
            try {
                UnequalTransactionMember member = (UnequalTransactionMember) spenders.get(i);
                float spent = member.getSpent();
                amounts.add(new Amount(users.get(i), transaction, spent));
                totalSpent += spent;
            } catch (Exception e) {
                throw new Exception("User not found.");
            }
        }

        if (Math.abs(totalSpent - transaction.getAmount()) > 0.01)
            throw new Exception("The total transaction amount is not equal to what each participant spent individually");
        return amounts;
    }
}
