package cz.cvut.fit.sp.chipin.base.amount.calculator;

import cz.cvut.fit.sp.chipin.authentication.user.User;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;
import cz.cvut.fit.sp.chipin.base.transaction.spender.ShareTransactionMember;

import java.util.ArrayList;
import java.util.List;

public class ShareBasedAmountCalculator implements AmountCalculator {
    @Override
    public List<Amount> calculateAmounts(List<User> users, Transaction transaction, List<MemberAbstractRequest> spenders) throws Exception {
        List<Amount> amounts = new ArrayList<>();

        Float shares = 0f;
        List<ShareTransactionMember> memberBySharesList = new ArrayList<>();
        for (MemberAbstractRequest member : spenders) {
            ShareTransactionMember memberByShares = (ShareTransactionMember) member;
            memberBySharesList.add(memberByShares);
            shares += memberByShares.getShare();
        }

        for (int i = 0; i < users.size(); i++) {
            try {
                Float percentage = memberBySharesList.get(i).getShare() / shares;
                amounts.add(new Amount(users.get(i), transaction, percentage * transaction.getConvertedAmount()));
            } catch (Exception e) {
                throw new Exception("User not found.");
            }
        }
        return amounts;
    }
}
