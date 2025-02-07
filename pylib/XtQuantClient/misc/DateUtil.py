
import datetime
import pytz


class DateUtil:
    """Date utility class"""

    # DATE_FMT_YMD = '%Y-%m-%d'
    # DATE_FMT_YMDHMS = '%Y-%m-%d %H:%M:%S'

    @staticmethod
    def nowTimestampMilli() -> int:
        """
        Get current millisecond timestamp
        """
        return int(datetime.datetime.now().timestamp() * 1000)

    @staticmethod
    def nowTimestamp() -> int:
        """
        Get current second timestamp
        """
        return int(datetime.datetime.now().timestamp())

    @staticmethod
    def nowString(fmt: str, zoneStr: str = None) -> str:
        """
        Get the current time string
        :param fmt: Time format
        :param zoneStr: Time zone string
        :return: Time string
        """
        tz = pytz.timezone(zoneStr) if zoneStr else None
        dt = datetime.datetime.now(tz)
        return dt.strftime(fmt)

    @staticmethod
    def curYmd(delimiter: str = None, zoneStr: str = None) -> str:
        """
        Get the current date string, the default format is %Y%m%d
        """
        if delimiter:
            return DateUtil.nowString('%Y' + delimiter + '%m' + delimiter + '%d', zoneStr)
        else:
            return DateUtil.nowString("%Y%m%d", zoneStr)

    @staticmethod
    def curYmdInt(zoneStr: str = None) -> int:
        """
        Get the current date integer, the default format is %Y%m%d
        """
        return int(DateUtil.curYmd(zoneStr))

    @staticmethod
    def curHms(delimiter: str = None, zoneStr: str = None) -> str:
        """
        Get the current time string, the default format is %H%M%S
        """
        if delimiter:
            return DateUtil.nowString('%H' + delimiter + '%M' + delimiter + '%S', zoneStr)
        else:
            return DateUtil.nowString("%H%M%S", zoneStr)

    @staticmethod
    def curHmsInt(zoneStr: str = None) -> int:
        """
        Get the current time integer, the default format is %H%M%S
        """
        return int(DateUtil.curHms(zoneStr))

    @staticmethod
    def parse(dateStr: str, fmt: str) -> datetime:
        return datetime.datetime.strptime(dateStr, fmt)

    @staticmethod
    def fmtTimestamp(ts: int or float, fmt: str, zoneStr: str = None) -> str:
        """
        Format timestamp
        :param ts: Timestamp
        :param fmt: Time format
        :param zoneStr: Time zone string
        :return: Time string
        """
        tz = pytz.timezone(zoneStr) if zoneStr else None
        dt = datetime.datetime.fromtimestamp(ts, tz)
        return dt.strftime(fmt)

    @staticmethod
    def timestamp2YmdInt(ts: int, zoneStr: str = None) -> int:
        """
        Convert timestamp to date integer
        """
        return int(DateUtil.fmtTimestamp(ts, '%Y%m%d', zoneStr))

    @staticmethod
    def timestamp2Ymd(ts: int, delimiter: str = None, zoneStr: str = None) -> str:
        """
        Convert timestamp to date string
        """
        if delimiter:
            return DateUtil.fmtTimestamp(ts, '%Y' + delimiter + '%m' + delimiter + '%d', zoneStr)
        else:
            return DateUtil.fmtTimestamp(ts, "%Y%m%d", zoneStr)


if __name__ == '__main__':
    print(DateUtil.nowTimestampMilli())
    print(DateUtil.nowTimestamp())
    print(DateUtil.nowString('%Y-%m-%d %H:%M:%S.%f', 'Asia/Shanghai'))
    print(DateUtil.curYmd())
    print(DateUtil.curYmdInt())
    print(DateUtil.curHms())
    print(DateUtil.curHmsInt())
    print(DateUtil.parse('2020-01-01 12:00:00', '%Y-%m-%d %H:%M:%S'))
    print(DateUtil.fmtTimestamp(1577836800, '%Y-%m-%d %H:%M:%S', 'Asia/Shanghai'))
    print(DateUtil.fmtTimestamp(1577836800.392, '%Y%m%d%H%M%S.%f', 'Asia/Shanghai'))
    print(DateUtil.timestamp2YmdInt(1577836800, 'Asia/Shanghai'))
    print(DateUtil.timestamp2Ymd(1577836800, '-', 'Asia/Shanghai'))
