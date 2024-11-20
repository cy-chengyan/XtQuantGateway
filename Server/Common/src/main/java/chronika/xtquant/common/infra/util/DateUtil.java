package chronika.xtquant.common.infra.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DateUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DTF_YYYY_MM_DD = DateTimeFormatter.ofPattern(YYYY_MM_DD);

    /**
     * Format millisecond to yyyy-MM-dd
     */
    public static String formatMillSecondToYmd(long millSecond) {
        Timestamp ts = new Timestamp(millSecond);
        return ts.toLocalDateTime().format(DTF_YYYY_MM_DD);
    }

    /**
     * Parse yyyy-MM-dd to millisecond
     */
    public static long parseYmdToMillSecond(String ymd) {
        return Timestamp.valueOf(ymd + " 00:00:00").getTime();
    }

    /**
     * Format millisecond to yyyy-MM-dd HH:mm:ss
     */
    public static String formatMillSecondToYmdHms(long millSecond) {
        Timestamp ts = new Timestamp(millSecond);
        return ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Parse yyyy-MM-dd HH:mm:ss to millisecond
     */
    public static long parseYmdHmsToMillSecond(String ymdHms) {
        return Timestamp.valueOf(ymdHms).getTime();
    }

    /**
     * Parse date object to yyyy-MM-dd string
     */
    public static String parseDateToYmd(Date date) {
        return DateFormatUtils.format(date, YYYY_MM_DD);
    }

    /**
     * Parse date object to yyyy-MM-dd HH:mm:ss string
     */
    public static String parseDateToYmdHms(Date date) {
        return DateFormatUtils.format(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * Get current date in yyyyMMdd format
     */
    public static Integer currentYmd() {
        return Integer.parseInt(DateFormatUtils.format(new Date(), "yyyyMMdd"));
    }

    /**
     * Get current millisecond
     */
    public static Long currentMillSecond() {
        return System.currentTimeMillis();
    }

    /**
     * 将 2024-03-22T16:06:30.910806+08:00 这样的时间字符串转换为毫秒
     */
    public static Long parseIsoStringToMillSecond(String s) {
        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(s);
        Instant instant = Instant.from(ta);
        return instant.toEpochMilli();
    }

    /**
     * 获取当前时间的字符串
     */
    public static String currentDatetimeStr(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

}
