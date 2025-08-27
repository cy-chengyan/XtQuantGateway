package chronika.quotation.market.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MarketStatus {

    UNKNOWN(0, "Unknown"),
    OPEN(1, "Open"),
    CLOSE(2, "Close"),
    EARLY_CLOSE(3, "EarlyClose"),
    ;

    @JsonValue
    private final int code;
    private final String desc;

    MarketStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
