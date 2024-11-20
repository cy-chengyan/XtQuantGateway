package chronika.xtquant.common.infra.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Bool {

    TRUE(1, "True"),
    FALSE(2, "False");

    @JsonValue
    private final int code;
    private final String desc;

    Bool(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Bool getByCode(int code) {
        for (Bool bool : Bool.values()) {
            if (bool.getCode() == code) {
                return bool;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
