package app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class CurrencyConvertService {
    public BigDecimal convert(BigDecimal amount, String from, String to,
                              Map<String, BigDecimal> rates,String base) {
        if  (from.equalsIgnoreCase(to)) {
            return amount.setScale(2, RoundingMode.HALF_EVEN);
        }
        if (from.equalsIgnoreCase(base)) {
            return amount.multiply(rates.get(to)).setScale(2, RoundingMode.HALF_EVEN);
        }
        if (to.equalsIgnoreCase(base)) {
            return amount.divide(rates.get(from), 2, RoundingMode.HALF_EVEN);
        }
        BigDecimal amountInBase = amount.divide(rates.get(from), 6, RoundingMode.HALF_EVEN);
        return amountInBase.multiply(rates.get(to)).setScale(2, RoundingMode.HALF_EVEN);
    }
}
