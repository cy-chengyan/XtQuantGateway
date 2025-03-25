package chronika.xtquant.common.infra.util;

public class BizUtil {

    public static String parseAccountId(String accountKey) {
        // accountKey格式可能如下:
        // 2____10075________49____620000308469____
        // 2____10355____10355____49____8883372637____
        // 取出 620000308469 或是 8883372637
        String[] parts = accountKey.split("____");
        if (parts.length < 5) {
            return null;
        }
        return parts[4];
    }

}
