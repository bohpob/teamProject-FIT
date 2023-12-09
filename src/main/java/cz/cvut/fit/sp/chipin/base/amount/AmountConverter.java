package cz.cvut.fit.sp.chipin.base.amount;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountConverter {
    public static AmountDTO toDto(Amount amount) {
        Float roundedAmount = BigDecimal.valueOf(amount.getAmount()).setScale(2, RoundingMode.HALF_UP).floatValue();
        return new AmountDTO(amount.getUser().getName(), roundedAmount);
    }
}
