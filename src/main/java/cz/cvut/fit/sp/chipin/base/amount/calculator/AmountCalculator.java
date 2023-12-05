package cz.cvut.fit.sp.chipin.base.amount.calculator;

import cz.cvut.fit.sp.chipin.authentication.useraccount.UserAccount;
import cz.cvut.fit.sp.chipin.base.amount.Amount;
import cz.cvut.fit.sp.chipin.base.transaction.Transaction;
import cz.cvut.fit.sp.chipin.base.transaction.spender.MemberAbstractRequest;

import java.util.List;

public interface AmountCalculator {
    List<Amount> calculateAmounts(List<UserAccount> users, Transaction transaction, List<MemberAbstractRequest> spenders) throws Exception;
}
