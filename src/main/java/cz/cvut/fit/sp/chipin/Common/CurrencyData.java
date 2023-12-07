package cz.cvut.fit.sp.chipin.Common;

import lombok.Getter;

import java.util.Currency;
import java.util.Map;

@Getter
public class CurrencyData {
    private boolean success;
    private boolean historical;
    private String date;
    private long timestamp;
    private String base;
    private Map<String, Double> rates;
}
