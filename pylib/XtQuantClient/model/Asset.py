
from decimal import Decimal
from XtQuantClient.misc import decXtQuantAssetPrecision
# from misc import decXtQuantAssetPrecision


class Asset:

    def __init__(self, id, accountId, date, totalAsset, marketValue, cash, withdrawableCash, profit, manualUpdatedAt, memo):
        self.id = id
        """交易网关中的数据表的主键ID, 无实际意义"""
        self.accountId = accountId
        """账号ID"""
        self.date = date
        """日期, format: YYYYMMDD"""
        self.totalAsset = Decimal(totalAsset).quantize(decXtQuantAssetPrecision)
        """总资产"""
        self.marketValue = Decimal(marketValue).quantize(decXtQuantAssetPrecision)
        """证券市值"""
        self.cash = Decimal(cash).quantize(decXtQuantAssetPrecision)
        """可用资金"""
        self.withdrawableCash = Decimal(withdrawableCash).quantize(decXtQuantAssetPrecision)
        """可取资金"""
        self.profit = Decimal(profit).quantize(decXtQuantAssetPrecision)
        """盈亏金额"""
        self.manualUpdatedAt = manualUpdatedAt
        """手动更新时间(毫秒)"""
        self.memo = memo
        """备注"""

    @staticmethod
    def fromJson(data):
        return Asset(
            data['id'],
            data['accountId'],
            data['date'],
            data['totalAsset'],
            data['marketValue'],
            data['cash'],
            data['withdrawableCash'],
            data['profit'],
            data['manualUpdatedAt'],
            data['memo']
        )
