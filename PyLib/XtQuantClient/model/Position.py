
from decimal import Decimal
from XtQuantClient.misc import decXtQuantPricePrecision, decXtQuantAssetPrecision
# from misc import decXtQuantPricePrecision, decXtQuantAssetPrecision

class Position:

    def __init__(self, id, accountId, date, stockCode, volume, canUseVolume, frozenVolume, onRoadVolume, openPrice, marketValue, yesterdayVolume, profit, manualUpdatedAt, memo):
        self.id = id
        """交易网关中的数据表的主键ID, 无实际意义"""
        self.accountId = accountId
        """账号ID"""
        self.date = date
        """日期, format: YYYYMMDD"""
        self.stockCode = stockCode
        """股票代码"""
        self.volume = volume
        """持仓数量"""
        self.canUseVolume = canUseVolume
        """可用数量"""
        self.frozenVolume = frozenVolume
        """冻结数量"""
        self.onRoadVolume = onRoadVolume
        """在途数量"""
        self.openPrice = Decimal(openPrice).quantize(decXtQuantPricePrecision)
        """平均建仓成本价"""
        self.marketValue = Decimal(marketValue).quantize(decXtQuantAssetPrecision)
        """市价"""
        self.yesterdayVolume = yesterdayVolume
        """昨日持仓数量"""
        self.profit = Decimal(profit).quantize(decXtQuantAssetPrecision)
        """盈亏"""
        self.manualUpdatedAt = manualUpdatedAt
        """手动更新时间(毫秒)"""
        self.memo = memo
        """备注"""

    @staticmethod
    def fromJson(data):
        return Position(
            data['id'],
            data['accountId'],
            data['date'],
            data['stockCode'],
            data['volume'],
            data['canUseVolume'],
            data['frozenVolume'],
            data['onRoadVolume'],
            data['openPrice'],
            data['marketValue'],
            data['yesterdayVolume'],
            data['profit'],
            data['manualUpdatedAt'],
            data['memo']
        )
