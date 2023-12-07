package cz.cvut.fit.sp.chipin.base.amount.calculator;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.spender.AdjustmentTransactionMember;

import java.util.ArrayList;
import java.util.List;

public class AdjustmentBasedAmountCalculator implements AmountCalculator {
    @Override
    public List<Amount> calculateAmounts(List<User> users, Transaction transaction, List<MemberAbstractRequest> spenders) throws Exception {
        List<Amount> amounts = new ArrayList<>();

        Float totalAdjustment = 0f;
        List<AdjustmentTransactionMember> adjustmentMemberList = new ArrayList<>();
        for (MemberAbstractRequest member : spenders) {
            AdjustmentTransactionMember memberByAdjustment = (AdjustmentTransactionMember) member;
            adjustmentMemberList.add(memberByAdjustment);
            totalAdjustment += memberByAdjustment.getAdjustment();
        }

        float amountBeforeAdjustment = transaction.getConvertedAmount() - totalAdjustment;
        Float baseAmount = amountBeforeAdjustment / adjustmentMemberList.size();

        for (int i = 0; i < users.size(); i++) {
            try {
                amounts.add(new Amount(users.get(i), transaction, adjustmentMemberList.get(i).getAdjustment() + baseAmount));
            } catch (Exception e) {
                throw new Exception("User not found.");
            }
        }
        return amounts;
    }
}
