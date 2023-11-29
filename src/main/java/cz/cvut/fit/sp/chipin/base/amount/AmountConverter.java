package cz.cvut.fit.sp.chipin.base.amount;

import java.util.Locale;

public class AmountConverter {
    public static AmountDTO toDto(Amount amount) {
        return new AmountDTO(amount.getUserAccount().getName(), Float.valueOf(String.format(Locale.getDefault(),
                "%.2f", amount.getAmount())));
    }
}
