package cz.cvut.fit.sp.chipin.base.amount.calculator;

import cz.cvut.fit.sp.chipin.base.transaction.TransactionType;

import java.util.HashMap;
import java.util.Map;

public class AmountSplitterProvider {
    private static final Map<TransactionType, AmountCalculator> strategyMap = new HashMap<>();

    static {
        strategyMap.put(TransactionType.EQUALLY, new EqualAmountCalculator());
        strategyMap.put(TransactionType.UNEQUALLY, new UnequalAmountCalculator());
        strategyMap.put(TransactionType.BY_PERCENTAGES, new PercentBasedAmountCalculator());
        strategyMap.put(TransactionType.BY_SHARES, new ShareBasedAmountCalculator());
        strategyMap.put(TransactionType.BY_ADJUSTMENT, new AdjustmentBasedAmountCalculator());
    }

    public static AmountCalculator getStrategy(TransactionType strategyEnum) {
        return strategyMap.get(strategyEnum);
    }
}
