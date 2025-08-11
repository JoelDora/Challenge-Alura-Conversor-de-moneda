package app.model;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Map;

public class ApiResponse {
    private String result;
    private String baseCode;


    private Map<String, BigDecimal> conversion_rates;

    public String getResult() {
        return result;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public Map<String, BigDecimal> getConversion_rates() {
        return conversion_rates;
    }
}
