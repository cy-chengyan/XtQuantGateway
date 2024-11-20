package chronika.xtquant.common.infra.enums;

import org.springframework.util.StringUtils;

/**
 * 应用环境类型
 */
public enum AppEnvType {

    TEST,
    STAGE,
    PROD,
    ;

    public static AppEnvType getByAppEnvString(String envStr) {
        if (!StringUtils.hasLength(envStr)) {
            return TEST;
        }

        String code = envStr.toUpperCase();
        for (AppEnvType appEnvType : AppEnvType.values()) {
            if (appEnvType.name().equals(code)) {
                return appEnvType;
            }
        }
        return TEST;
    }

}
